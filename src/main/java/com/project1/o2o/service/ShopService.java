package com.project1.o2o.service;

import java.io.InputStream;

import com.project1.o2o.dto.ShopExecution;
import com.project1.o2o.entity.Shop;
import com.project1.o2o.exceptions.ShopOperationException;

public interface ShopService {
	/**
	 * get shop information through shopId
	 * 
	 * @param shopId
	 * @return
	 */
	Shop getByShopId(long shopId);
	
	/**
	 * Update shop information, including image processing
	 * Any modifying of shop will return ShopExecution from dto
	 * 
	 * @param shop
	 * @param shopImgInputStream
	 * @param fileName
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException;
	
	/**
	 * Register shop information, including image processing
	 * 
	 * @param shop
	 * @param shopImgInputStream
	 * @param fileName
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException;
}
