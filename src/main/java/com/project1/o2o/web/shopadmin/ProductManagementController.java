package com.project1.o2o.web.shopadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project1.o2o.dto.ImageConstructor;
import com.project1.o2o.dto.ProductExecution;
import com.project1.o2o.entity.Product;
import com.project1.o2o.entity.Shop;
import com.project1.o2o.enums.ProductStateEnum;
import com.project1.o2o.exceptions.ProductOperationException;
import com.project1.o2o.service.ProductService;
import com.project1.o2o.util.CodeUtil;
import com.project1.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {
	@Autowired
	private ProductService productService;
	
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
		MultipartHttpServletRequest multipartRequest = null; //process file input stream
		ImageConstructor thumbnail = null; //process thumbnail, store stream and name
		List<ImageConstructor> productImgList = new ArrayList<ImageConstructor>(); //store input stream list and name list
		//extracts file input stream from request session
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		try {
			//if request contains input stream, extract necessary files (product image and thumbnail)
			if(multipartResolver.isMultipart(request)) { //exists necessary files
				multipartRequest = (MultipartHttpServletRequest) request;
				//Extract thumbnail and construct ImageConstructor target
				CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail"); //key = thumbnail
				thumbnail = new ImageConstructor(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
				//Extract product image list and construct List<ImageConstructor> target, maximum 6 images allowed
				for (int i = 0; i < IMAGEMAXCOUNT; i++) {
					CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest.getFile("productImg" + i);
					if(productImgFile != null) { //if not null, then there is image to process. add it to List
						ImageConstructor productImg = new ImageConstructor(productImgFile.getOriginalFilename(),
								productImgFile.getInputStream());
						productImgList.add(productImg);
					} else break; //if i'th input stream is null, there are no more images uploaded
				}
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
}
