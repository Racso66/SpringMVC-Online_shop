package com.project1.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.project1.o2o.BaseTest;
import com.project1.o2o.entity.Area;
import com.project1.o2o.entity.Shop;
import com.project1.o2o.entity.ShopCategory;
import com.project1.o2o.entity.UserInfo;

public class ShopDaoTest extends BaseTest{
	@Autowired
	private ShopDao shopDao;
	@Test
	@Ignore
	public void testInsertShop() {
		Shop shop = new Shop();
		UserInfo owner = new UserInfo();
		Area area = new Area();
		ShopCategory shopCategory = new ShopCategory();
		owner.setUserId(1L); //for long put L
		area.setAreaId(2);
		shopCategory.setShopCategoryId(1L);
		shop.setOwner(owner);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setShopName("Test Store");
		shop.setShopDesc("Test Description");
		shop.setShopAddr("Test Address");
		shop.setPhone("Test Phone number");
		shop.setShopImg("Test image");
		shop.setCreateTime(new Date());
		shop.setEnableStatus(1);
		shop.setAdvice("Verifying");
		int effectedNum = shopDao.insertShop(shop);
		assertEquals(1, effectedNum);
	}
	@Test
	public void testUpdateShop() {
		Shop shop = new Shop();
		shop.setShopId(2L);
		shop.setShopDesc("Test Update Description");
		shop.setShopAddr("Test Update Address");
		shop.setLastEdited(new Date());
		int effectedNum = shopDao.updateShop(shop);
		assertEquals(1, effectedNum);
	}
}
