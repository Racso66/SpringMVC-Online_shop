package com.project1.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.project1.o2o.BaseTest;
import com.project1.o2o.entity.ShopCategory;

public class ShopCategoryDaoTest extends BaseTest{
	@Autowired
	private ShopCategoryDao shopCategoryDao;
	
	@Test
	public void testBQueryShopCategory() {
		List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(null);
		System.out.println(shopCategoryList.size());
	}
	
	@Test
	public void testAInsertShopCategory() {
		ShopCategory shopCategory = new ShopCategory();
		shopCategory.setShopCategoryName("Sushi");
		shopCategory.setShopCategoryDesc("Japanese sushi");
		shopCategory.setShopCategoryImg("Test image");
		shopCategory.setPriority(1);
		shopCategory.setCreateTime(new Date());
		
		int effectedNum = shopCategoryDao.insertShopCategory(shopCategory);
		assertEquals(1, effectedNum);
	}
	
	@Test
	public void testCDeleteShopCategory() throws Exception {
		Long shopCategoryId = 4L;
		ShopCategory shopCategory = shopCategoryDao.queryShopCategoryById(shopCategoryId);
		if("Sushi".equals(shopCategory.getShopCategoryName())){
			int effectedNum = shopCategoryDao.deleteShopCategory(shopCategoryId);
			assertEquals(1, effectedNum);
		}
	}
}
