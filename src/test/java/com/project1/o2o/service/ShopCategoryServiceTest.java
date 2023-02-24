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
import com.project1.o2o.dto.ShopCategoryExecution;
import com.project1.o2o.entity.ShopCategory;
import com.project1.o2o.enums.ShopCategoryStateEnum;
import com.project1.o2o.exceptions.ShopCategoryOperationException;

public class ShopCategoryServiceTest extends BaseTest {
	@Autowired
	private ShopCategoryService shopCategoryService;
	
	@Test
	public void testAAddShopCategory() throws ShopCategoryOperationException, FileNotFoundException{
		ShopCategory shopCategory = new ShopCategory();
		shopCategory.setCreateTime(new Date());
		shopCategory.setPriority(0);
		shopCategory.setShopCategoryName("Educations");
		shopCategory.setShopCategoryDesc("Educations");
		File thumbnailFile = new File("C:\\Users\\SAO\\Desktop\\Dinoland pics\\restaurant.png");
		InputStream is = new FileInputStream(thumbnailFile);
		ImageConstructor thumbnail = new ImageConstructor(thumbnailFile.getName(), is);
		ShopCategoryExecution sce = shopCategoryService.addShopCategory(shopCategory, thumbnail);
		assertEquals(ShopCategoryStateEnum.SUCCESS.getState(), sce.getState());
	}
	
	@Test
	@Ignore
	public void testBUpdateShopCategory() throws ShopCategoryOperationException, FileNotFoundException{
		ShopCategory shopCategory = new ShopCategory();
		shopCategory.setPriority(10);
		shopCategory.setShopCategoryId(8L);
		shopCategory.setShopCategoryName("Restaurant");
		shopCategory.setShopCategoryDesc("Food and beverage");
		shopCategory.setParent(null);
		File shopCategoryImg = new File("C:\\Users\\SAO\\Desktop\\Dinoland pics\\restaurant.png");
		InputStream is = new FileInputStream(shopCategoryImg);
		ImageConstructor imageConstructor = new ImageConstructor(shopCategoryImg.getName(), is);
		ShopCategoryExecution shopCategoryExecution = shopCategoryService.modifyShopCategory(shopCategory,imageConstructor);
		System.out.println("new file path is:" + shopCategoryExecution.getShopCategory().getShopCategoryImg());
	}
}
