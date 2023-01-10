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
}
