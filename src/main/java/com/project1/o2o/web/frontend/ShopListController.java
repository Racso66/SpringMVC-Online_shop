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

import com.project1.o2o.dto.ShopExecution;
import com.project1.o2o.entity.Area;
import com.project1.o2o.entity.Shop;
import com.project1.o2o.entity.ShopCategory;
import com.project1.o2o.service.AreaService;
import com.project1.o2o.service.ShopCategoryService;
import com.project1.o2o.service.ShopService;
import com.project1.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class ShopListController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;
	
	/**
	 * Return shop category list(first level or its child) and areaList in each shop page
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "listshopspageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShopsPageInfo(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//get parentId from request
		long parentId = HttpServletRequestUtil.getLong(request, "parentId");
		List<ShopCategory> shopCategoryList = null;
		if(parentId != -1) {
			//if there exists parentId, retrieve all child categories under the parent
			try {
				ShopCategory shopCategoryCondition = new ShopCategory();
				ShopCategory parent = new ShopCategory();
				parent.setShopCategoryId(parentId);
				shopCategoryCondition.setParent(parent);
				shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
			} catch(Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		} else {
			//if there isn't parentId provided, retrieve all first level categories
			try {
				shopCategoryList = shopCategoryService.getShopCategoryList(null);
			} catch(Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		}
		modelMap.put("shopCategoryList", shopCategoryList);
		List<Area> areaList = null;
		try {
			// retrieve area list info
			areaList = areaService.getAreaList();
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
			return modelMap;
		} catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	
	@RequestMapping(value = "listshops", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShops(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//retrieve page inxdex and page size(number of data to be shown on each page) from front-end
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		if((pageIndex >= 0) && (pageSize>= 0)) {
			//retrieve (if there is any) first and second level category id, area id, shop name used for search
			long parentId = HttpServletRequestUtil.getLong(request, "parentId");
			long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
			int areaId = HttpServletRequestUtil.getInt(request, "areaId");
			String shopName = HttpServletRequestUtil.getString(request, "shopName");
			//concatenate above search conditions
			Shop shopCondition = concatShopCondition(parentId, shopCategoryId, areaId, shopName);
			ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("count", se.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "Please enter both pageIndex and pageSize to search");
		}
		return modelMap;
	}

	private Shop concatShopCondition(long parentId, long shopCategoryId, int areaId, String shopName) {
		Shop shopCondition = new Shop();
		if(parentId != -1L) {
			//search for all child categories under the given parent category id
			ShopCategory childCategory = new ShopCategory();
			ShopCategory parentCategory = new ShopCategory();
			parentCategory.setShopCategoryId(parentId);
			childCategory.setParent(parentCategory);
			shopCondition.setShopCategory(childCategory);
		}
		if(shopCategoryId != -1L) {
			//search for all shops under the given shopCategory id
			ShopCategory shopCategory = new ShopCategory();
			shopCategory.setShopCategoryId(shopCategoryId);
			shopCondition.setShopCategory(shopCategory);
		}
		if(areaId != -1L) {
			//search for all shops in the given area
			Area area = new Area();
			area.setAreaId(areaId);
			shopCondition.setArea(area);
		}
		if(shopName != null) {
			//Search for all shops with name containing "shopName"
			shopCondition.setShopName(shopName);
		}
		//All shops that appear in front-end showcase system are valid shops (enable status = 1)
		shopCondition.setEnableStatus(1);
		return shopCondition;
	}
}
