package com.project1.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.project1.o2o.entity.Product;

public interface ProductDao {
	/**
	 * 
	 * @param product
	 * @return
	 */
	int insertProduct(Product product);

	/**
	 * get product information by productId
	 * 
	 * @param productId
	 * @return
	 */
	Product queryByProductId(long productId);

	/**
	 * update product info
	 * 
	 * @param product
	 * @return
	 */
	int updateProduct(Product product);
	
	/**
	 * Search for list of products and separate to pages using following : Product name(fuzzy), status, shopId, category
	 * @param productCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<Product> queryProductList(@Param("productCondition") Product productCondition, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);
	
	/**
	 * search for number of total products (with same condition as queryProductList, how many products exists in the list)
	 * @param productCondition
	 * @return
	 */
	int queryProductCount(@Param("productCondition") Product productCondition);
	
	/**
	 * Before deleting product, set its category id to null
	 * @param productCategoryId
	 * @return
	 */
	int updateProductCategoryToNull(long productCategoryId);
}
