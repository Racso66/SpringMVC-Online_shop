package com.project1.o2o.web.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project1.o2o.entity.HeadLine;
import com.project1.o2o.entity.ShopCategory;
import com.project1.o2o.service.HeadLineService;
import com.project1.o2o.service.ShopCategoryService;

@Controller
@RequestMapping("/frontend")
public class FrontPageController {
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private HeadLineService headLineService;
	
	/**
	 * initialize front-end showcase system information. (retrieve first level shop category list and head line list)
	 * @return
	 */
	@RequestMapping(value = "/listfrontpageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listFrontPageInfo(){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		//retrieve first level shop category list (shop categories with parent id == null)
		try {
			shopCategoryList = shopCategoryService.getShopCategoryList(null);//parent id = null
			modelMap.put("shopCategoryList", shopCategoryList);
		} catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		//retrieve head line info
		List<HeadLine> headLineList = new ArrayList<HeadLine>();
		try {
			HeadLine headLineCondition = new HeadLine();
			headLineCondition.setEnableStatus(1);
			headLineList = headLineService.getHeadLineList(headLineCondition);
			modelMap.put("headLineList", headLineList);
		} catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		modelMap.put("success", true);
		return modelMap;
	}
}
