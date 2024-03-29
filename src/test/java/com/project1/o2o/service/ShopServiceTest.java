package com.project1.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.project1.o2o.BaseTest;
import com.project1.o2o.dto.ImageConstructor;
import com.project1.o2o.dto.ShopExecution;
import com.project1.o2o.entity.Area;
import com.project1.o2o.entity.Shop;
import com.project1.o2o.entity.ShopCategory;
import com.project1.o2o.entity.UserInfo;
import com.project1.o2o.enums.ShopStateEnum;
import com.project1.o2o.exceptions.ShopOperationException;

public class ShopServiceTest extends BaseTest{
	@Autowired
	private ShopService shopService;
	
	@Test
	public void testGetShopList() {
		Shop shopCondition = new Shop();
		ShopCategory sc = new ShopCategory();
		sc.setShopCategoryId(1L);
		shopCondition.setShopCategory(sc);
		ShopExecution se = shopService.getShopList(shopCondition, 0, 3);
		System.out.println("number of shop list: " + se.getShopList().size());
		System.out.println("Total number of shop list: " + se.getCount());
	}
	
	@Test
	@Ignore
	public void testModifyShop() throws ShopOperationException, FileNotFoundException {
		Shop shop = new Shop();
		shop.setShopId(2L);
		shop.setShopName("Name after modify");
		File shopImg = new File("C:\\Users\\SAO\\Desktop\\Dinoland pics\\1.png");
		InputStream is = new FileInputStream(shopImg);
		ImageConstructor imageConstructor = new ImageConstructor("1.png", is);
		ShopExecution shopExecution = shopService.modifyShop(shop,imageConstructor);
		System.out.println("new file path is:" + shopExecution.getShop().getShopImg());
	}
	@Test
	@Ignore
	public void testAddShop() throws FileNotFoundException {
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
		shop.setShopName("Test Store 3");
		shop.setShopDesc("Test Description 3");
		shop.setShopAddr("Test Address 3");
		shop.setPhone("Test Phone number 3");
		shop.setCreateTime(new Date());
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		shop.setAdvice("Verifying");
		File shopImg = new File("C:\\Users\\SAO\\Desktop\\Dinoland pics\\2.jpg");
		InputStream is = new FileInputStream(shopImg);
		ImageConstructor imageConstructor = new ImageConstructor("1.png", is);
		ShopExecution se = shopService.addShop(shop, imageConstructor);
		assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
	}

}
