package com.project1.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

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
		shopCategory.setPriority(8);
		shopCategory.setShopCategoryName("Education");
		shopCategory.setShopCategoryDesc("Education");
		File thumbnailFile = new File("C:\\Users\\SAO\\Desktop\\Dinoland pics\\4.png");
		InputStream is = new FileInputStream(thumbnailFile);
		ImageConstructor thumbnail = new ImageConstructor(thumbnailFile.getName(), is);
		ShopCategoryExecution sce = shopCategoryService.addShopCategory(shopCategory, thumbnail);
		assertEquals(ShopCategoryStateEnum.SUCCESS.getState(), sce.getState());
	}
}
