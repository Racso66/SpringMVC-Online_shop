package com.project1.o2o.service;

import java.util.List;

import com.project1.o2o.dto.ImageConstructor;
import com.project1.o2o.dto.ProductExecution;
import com.project1.o2o.entity.Product;
import com.project1.o2o.exceptions.ProductOperationException;

public interface ProductService {
	/**
	 * Specify product information and image processing
	 * 
	 * @param product
	 * @param thumbnail      also contains its name
	 * @param productImgList detailed image of products and every img in list has its own name
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution addProduct(Product product, ImageConstructor thumbnail, List<ImageConstructor> productImgList)
		throws ProductOperationException;
	
	/**
	 * get seleced product info by product id
	 * @param productId
	 * @return
	 */
	Product getProductById(long productId);
	
	/**
	 * modifies product info and image processing
	 * @param product
	 * @param thumbnail
	 * @param productImgList
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution modifyProduct(Product product, ImageConstructor thumbnail, List<ImageConstructor> productImgList)
		throws ProductOperationException;
	
	/**
	 * Search for product list and perform pagination. (through product name[fuzzy], product state, shop id, product category
	 * @param productCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);
}
