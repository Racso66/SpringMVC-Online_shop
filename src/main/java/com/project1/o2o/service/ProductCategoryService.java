package com.project1.o2o.service;

import java.util.List;

import com.project1.o2o.entity.ProductCategory;

public interface ProductCategoryService {
	/**
	 * Search all product categories under the input shopId
	 * @param shopId
	 * @return List<ProductCategory>
	 */
	List<ProductCategory> getProductCategoryList(long shopId);
	
	ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
		throws ProductCategoryOperationException;
}
