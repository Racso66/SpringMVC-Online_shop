package com.project1.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.project1.o2o.entity.Shop;

public interface ShopDao {
	/**
	 * Find a list of shops by using the following options(fuzzy search): Shop name, Shop status, Shop category, area id, owner
	 * 
	 * Adding unique parameter to acquire correct values
	 * @param shopCondition
	 * @param rowIndex indicates the row from which the data is retrieved.
	 * @param pageSize how many rows of information to be returned.
	 * @return
	 */
	List<Shop>queryShopList(@Param("shopCondition")Shop shopCondition, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
	
	/**
	 * return count of "queryShoplist"
	 * @param shopCondition
	 * @return
	 */
	int queryShopCount(@Param("shopCondition") Shop shopCondition);
	
	/**
	 * Create new shops
	 * 
	 * @param shop
	 * @return = 1 successfully insert shop info, = -1 failed provided by myBatis
	 * After creating interface, need a class to implement == need a mapper .xml
	 * file
	 */
	int insertShop(Shop shop);
	
	/**
	 * Search for shop by using shop id
	 * 
	 * @param shopId
	 * @return
	 */
	Shop queryByShopId(long shopId);

	/*
	 * Update existing shops
	 * 
	 * @param shop
	 * @return
	 */
	int updateShop(Shop shop);
}
