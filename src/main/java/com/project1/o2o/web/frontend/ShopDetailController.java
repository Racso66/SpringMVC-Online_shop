package com.project1.o2o.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project1.o2o.dto.ProductExecution;
import com.project1.o2o.entity.Product;
import com.project1.o2o.entity.ProductCategory;
import com.project1.o2o.entity.Shop;
import com.project1.o2o.service.ProductCategoryService;
import com.project1.o2o.service.ProductService;
import com.project1.o2o.service.ShopService;
import com.project1.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class ShopDetailController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductCategoryService productCategoryService;
	
	/**
	 * Retrieve shop information and its category list
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listshopdetailpageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShopDetailPageInfo(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		Shop shop = null;
		List<ProductCategory> productCategoryList = null;
		if(shopId != -1) {
			//get shop info
			shop = shopService.getByShopId(shopId);
			//get all categories under the shop
			productCategoryList = productCategoryService.getProductCategoryList(shopId);
			modelMap.put("shop", shop);
			modelMap.put("productCategoryList", productCategoryList);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "No shopId");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/listproductsbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listProductsByShop(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		//None of above can be null
		if((pageIndex >= 0) && (pageSize >= 0) && (shopId >= 0)) {
			//search condition (optional)
			long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
			String productName = HttpServletRequestUtil.getString(request, "productName");
			Product productCondition = concatProductCondition(shopId, productCategoryId, productName);
			ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "index/size/shopid missing");
		}
		return modelMap;
	}

	private Product concatProductCondition(long shopId, long productCategoryId, String productName) {
		Shop shop = new Shop();
		Product productCondition = new Product();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		if(productCategoryId != -1L) {
			//search for all product under the given productCategory id
			ProductCategory productCategory = new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		if(productName != null) {
			//Search for all products with name containing "productName"
			productCondition.setProductName(productName);
		}
		//All shops that appear in front-end showcase system are valid shops (enable status = 1)
		productCondition.setEnableStatus(1);
		return productCondition;
	}
}
