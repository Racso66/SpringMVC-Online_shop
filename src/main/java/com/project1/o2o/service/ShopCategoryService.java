package com.project1.o2o.service;

import java.util.List;

import com.project1.o2o.dto.ImageConstructor;
import com.project1.o2o.dto.ShopCategoryExecution;
import com.project1.o2o.entity.ShopCategory;

public interface ShopCategoryService {
	/**
	 * 
	 * @param shopCategoryCondition
	 * @return
	 */
	List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
	
	/**
	 * add shopcategory and store category image
	 * 
	 * @param shopCategory
	 * @param thumbnail
	 * @return
	 */
	ShopCategoryExecution addShopCategory(ShopCategory shopCategory, ImageConstructor thumbnail);

	/**
	 * 
	 * @param shopCategory
	 * @param thumbnail
	 * @return
	 */
	ShopCategoryExecution modifyShopCategory(ShopCategory shopCategory, ImageConstructor thumbnail);

	/**
	 * return category info based on id
	 * 
	 * @param shopCategoryId
	 * @return
	 */
	ShopCategory getShopCategoryById(Long shopCategoryId);
}
