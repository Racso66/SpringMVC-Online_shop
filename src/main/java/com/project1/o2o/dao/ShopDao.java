package com.project1.o2o.dao;

import com.project1.o2o.entity.Shop;

public interface ShopDao {
	/*
	 * Create new shops
	 * 
	 * @param shop
	 * @return = 1 successfully insert shop info, = -1 failed provided by myBatis
	 * After creating interface, need a class to implement == need a mapper .xml
	 * file
	 */
	int insertShop(Shop shop);

	/*
	 * Update existing shops
	 * 
	 * @param shop
	 * @return
	 */
	int updateShop(Shop shop);
}
