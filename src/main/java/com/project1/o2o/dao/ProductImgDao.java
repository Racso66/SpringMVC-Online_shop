package com.project1.o2o.dao;

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
	 * @param productId
	 * @return
	 */
	Product queryByProductId(long productId);
	
	/**
	 * update product info
	 * @param product
	 * @return
	 */
	int updateProduct(Product product);
}
