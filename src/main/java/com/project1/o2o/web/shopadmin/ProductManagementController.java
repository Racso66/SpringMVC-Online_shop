package com.project1.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project1.o2o.dto.ImageConstructor;
import com.project1.o2o.dto.ProductExecution;
import com.project1.o2o.entity.Product;
import com.project1.o2o.entity.ProductCategory;
import com.project1.o2o.entity.Shop;
import com.project1.o2o.enums.ProductStateEnum;
import com.project1.o2o.exceptions.ProductOperationException;
import com.project1.o2o.service.ProductCategoryService;
import com.project1.o2o.service.ProductService;
import com.project1.o2o.util.CodeUtil;
import com.project1.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductCategoryService productCategoryService;
	
	//set maximum number of product specific images allowed to upload
	private static final int IMAGEMAXCOUNT = 6;
	
	@RequestMapping(value = "/addproduct", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addProduct(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//Kaptcha Verification code
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "Wrong code input");
			return modelMap;
		}
		//receive front-end starting values, including product, image list, thumbnail
		ObjectMapper mapper = new ObjectMapper();
		Product product = null;
		String productStr = HttpServletRequestUtil.getString(request, "productStr"); //receives JSON -> String from front-end
		ImageConstructor thumbnail = null; //process thumbnail, store stream and name
		List<ImageConstructor> productImgList = new ArrayList<ImageConstructor>(); //store input stream list and name list
		//extracts file input stream from request session
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		try {
			//if request contains input stream, extract necessary files (product image and thumbnail)
			if(multipartResolver.isMultipart(request)) { //exists necessary files
				thumbnail = handleImage(request, thumbnail, productImgList);
			} else { //error No images uploaded
				modelMap.put("success", false);
				modelMap.put("errMsg", "Upload at least 1 image");
				return modelMap;
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		try {
			//try to convert string input streams received from front-end to Product entity class using jackson mapper
			product = mapper.readValue(productStr, Product.class);
		}catch (Exception e){
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		// if product information, thumbnail, and image list are not empty, execute add product operation
		if (product != null && thumbnail != null && productImgList.size() > 0) {
			try {
				//from session, receive current shop id and set it to product. (reduce dependency of front-end data)
				Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
				product.setShop(currentShop);
				//execute add product operation
				ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);
				if(pe.getState() == ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			}catch (ProductOperationException e){
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		} else {//all 3 information are required
			modelMap.put("success", false);
			modelMap.put("errMsg", "Please enter required product information");
		}
		return modelMap;
	}

	private ImageConstructor handleImage(HttpServletRequest request, ImageConstructor thumbnail,
			List<ImageConstructor> productImgList) throws IOException {
		MultipartHttpServletRequest multipartRequest;
		multipartRequest = (MultipartHttpServletRequest) request;
		//Extract thumbnail and construct ImageConstructor target
		CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail"); //key = thumbnail
		if(thumbnailFile != null) thumbnail = new ImageConstructor(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
		//Extract product image list and construct List<ImageConstructor> target, maximum 6 images allowed
		for (int i = 0; i < IMAGEMAXCOUNT; i++) {
			CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest.getFile("productImg" + i);
			if(productImgFile != null) { //if not null, then there is image to process. add it to List
				ImageConstructor productImg = new ImageConstructor(productImgFile.getOriginalFilename(),
						productImgFile.getInputStream());
				productImgList.add(productImg);
			} else break; //if i'th input stream is null, there are no more images uploaded
		}
		return thumbnail;
	}
	
	/**
	 * get product info by product id
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/getproductbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getProductById(@RequestParam Long productId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// check empty input
		if(productId >= 0) {
			//retrieve product info
			Product product = productService.getProductById(productId);
			//retrieve product category list under the shop
			List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(product.getShop().getShopId());
			modelMap.put("product", product);
			modelMap.put("productCategoryList", productCategoryList);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "Enter productId");
		}
		return modelMap;
	}
	
	/**
	 * Modify product
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyproduct", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyProduct(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//determine if modify is for managing products or changing validation of a product.
		boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");//managing product requires verify code on new page
		if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "Wrong verify code entered");
			return modelMap;
		}
		// receive starting values from front end
		ObjectMapper mapper = new ObjectMapper();
		Product product = null;
		ImageConstructor thumbnail = null;
		List<ImageConstructor> productImgList = new ArrayList<ImageConstructor>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		//Extract files if there exists input stream
		try {
			if(multipartResolver.isMultipart(request)) {
				thumbnail = handleImage(request, thumbnail, productImgList);
			}
		} catch (Exception e){
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		//Receive form data String stream from front-end and change to product class
		try {
			String productStr = HttpServletRequestUtil.getString(request, "productStr");
			product = mapper.readValue(productStr, Product.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		//update product if not null
		if(product != null) {
			try {
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				Shop shop = new Shop();
				shop.setShopId(currentShop.getShopId());
				product.setShop(shop);
				//perform changing product information
				ProductExecution pe = productService.modifyProduct(product, thumbnail, productImgList);
				if(pe.getState() == ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (ProductOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "Enter product information");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/getproductlistbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getProductListByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//store pageindex and pagesize from front-end
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		//shopId from current session
		Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
		if((pageIndex >= 0) && (pageSize >= 0) && (currentShop != null) && (currentShop.getShopId() != null)) {
			//get required condition for input (which product category or product name is needed to select the list under a shop)
			//selection condition can be arranged
			long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
			String productName = HttpServletRequestUtil.getString(request, "productName");
			Product productCondition = packProductCondition(currentShop.getShopId(), productCategoryId, productName);
			//import search condition and pagination info for searching and return the according product list and count
			ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "Enter all values for pageSize, pageIndex and shopId");
		}
		return modelMap;
	}

	private Product packProductCondition(long shopId, long productCategoryId, String productName) {
		Product productCondition = new Product();
		Shop shop = new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		//if there is specified category
		if(productCategoryId != -1L) {
			ProductCategory productCategory = new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		//if there is fuzzy search for product name
		if(productName != null) productCondition.setProductName(productName);
		return productCondition;
	}
}
