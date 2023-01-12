package com.project1.o2o.service;

import com.project1.o2o.dto.ImageConstructor;
import com.project1.o2o.dto.ShopExecution;
import com.project1.o2o.entity.Shop;
import com.project1.o2o.exceptions.ShopOperationException;

public interface ShopService {
	/**
	 * Split page information and return shop lists according to shopCondition
	 * @param shopCondition
	 * @param pageIndex changed to pageIndex because front end only uses pages. Made pageCalculator util for transition
	 * @param pageSize
	 * @return
	 */
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);
	
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
	ShopExecution modifyShop(Shop shop, ImageConstructor thumbnail) throws ShopOperationException;
	
	/**
	 * Register shop information, including image processing
	 * 
	 * @param shop
	 * @param shopImgInputStream
	 * @param fileName
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution addShop(Shop shop, ImageConstructor thumbnail) throws ShopOperationException;
}
