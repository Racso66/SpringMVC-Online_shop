package com.project1.o2o.service;

import java.util.List;

import com.project1.o2o.dto.ProductCategoryExecution;
import com.project1.o2o.entity.ProductCategory;
import com.project1.o2o.exceptions.ProductCategoryOperationException;

public interface ProductCategoryService {
	/**
	 * Search all product categories under the input shopId
	 * 
	 * @param shopId
	 * @return List<ProductCategory>
	 * @throws RuntimeException
	 */
	List<ProductCategory> getProductCategoryList(long shopId);
	
	/**
	 * 
	 * @param productCategoryList
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException;
	
	/**
	 * This method needs to reset the product category id of the products under the category, then delete the category
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException;
}
