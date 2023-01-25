package com.project1.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.project1.o2o.entity.ShopCategory;

public interface ShopCategoryDao {
	/**
	 * return shop category list based on search condition
	 * 
	 * @param shopCategoryCondition
	 * @return
	 */
	List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);

	/**
	 * return shop category info through category id
	 * 
	 * @param shopCategoryId
	 * @return
	 */
	ShopCategory queryShopCategoryById(long shopCategoryId);

	/**
	 * insert shop category information
	 * 
	 * @param shopCategory
	 * @return
	 */
	int insertShopCategory(ShopCategory shopCategory);
	
	/**
	 * update info
	 * 
	 * @param shopCategory
	 * @return
	 */
	int updateShopCategory(ShopCategory shopCategory);

	/**
	 * 
	 * @param shopCategoryId
	 * @return
	 */
	int deleteShopCategory(long shopCategoryId);
	
	/**
	 * batch delete category with list of category id
	 * 
	 * @param shopCategoryIdList
	 * @return
	 */
	int batchDeleteShopCategory(List<Long> shopCategoryIdList);
}
