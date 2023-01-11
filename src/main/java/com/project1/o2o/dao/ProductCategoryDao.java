package com.project1.o2o.dao;

import java.util.List;

import com.project1.o2o.entity.ProductCategory;

public interface ProductCategoryDao {
	/**
	 * returns all product categories under the input shopId
	 * @param shopId
	 * @return List<ProductCategory>
	 */
	List<ProductCategory> queryProductCategoryList(long shopId);
		
	/**
	 * batch create product category
	 * @param productCategoryList
	 * @return
	 */
	int batchInsertProductCategory(List<ProductCategory> productCategoryList);
	
	/**
	 * delete productCategory by specified shopId
	 * @param productCategoryId, shopId
	 * @return effectedNum
	 */
	int deleteProductCategory(@Param("productCategoryId") long productCategoryId, @Param("shopId") long shopId);
}
