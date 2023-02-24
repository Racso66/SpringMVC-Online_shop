package com.project1.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.project1.o2o.BaseTest;
import com.project1.o2o.dto.ImageConstructor;
import com.project1.o2o.dto.ProductExecution;
import com.project1.o2o.entity.Product;
import com.project1.o2o.entity.ProductCategory;
import com.project1.o2o.entity.Shop;
import com.project1.o2o.enums.ProductStateEnum;
import com.project1.o2o.exceptions.ShopOperationException;

public class ProductServiceTest extends BaseTest {
	@Autowired
	private ProductService productService;
	
	@Test
	@Ignore
	public void testAAddProduct() throws ShopOperationException, FileNotFoundException{
		Product product = new Product();
		Shop shop = new Shop();
		shop.setShopId(2L);
		ProductCategory pc = new ProductCategory();
		pc.setProductCategoryId(2L);
		product.setShop(shop);
		product.setProductName("Test Product 1");
		product.setProductDesc("Test product 1");
		product.setProductCategory(pc);
		product.setPriority(20);
		product.setCreateTime(new Date());
		product.setEnableStatus(ProductStateEnum.SUCCESS.getState());
		//create thumbnail file input stream
		File thumbnailFile = new File("C:\\Users\\SAO\\Desktop\\Dinoland pics\\2.jpg");
		InputStream is = new FileInputStream(thumbnailFile);
		ImageConstructor thumbnail = new ImageConstructor(thumbnailFile.getName(), is);
		//test by adding 2 product specific image input stream and add to a list
		File productImg1 = new File("C:\\Users\\SAO\\Desktop\\Dinoland pics\\2.jpg");
		InputStream is1 = new FileInputStream(productImg1);
		File productImg2 = new File("C:\\Users\\SAO\\Desktop\\Dinoland pics\\3.png");
		InputStream is2 = new FileInputStream(productImg2);
		List<ImageConstructor> productImgList = new ArrayList<ImageConstructor>();
		productImgList.add(new ImageConstructor(productImg1.getName(), is1));
		productImgList.add(new ImageConstructor(productImg2.getName(), is2));
		//Verify by adding the products
		ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);
		assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());
	}
	
	@Test
	public void testModifyProduct() throws ShopOperationException, FileNotFoundException{
		Product product = new Product();
		Shop shop = new Shop();
		shop.setShopId(2L);
		ProductCategory pc = new ProductCategory();
		pc.setProductCategoryId(3L);
		product.setProductId(2L);
		product.setShop(shop);
		product.setProductCategory(pc);
		product.setProductName("Formal Product");
		product.setProductDesc("Formal product");
		//create thumbnail file input stream
		File thumbnailFile = new File("C:\\Users\\SAO\\Desktop\\Dinoland pics\\bubble-tea.png");
		InputStream is = new FileInputStream(thumbnailFile);
		ImageConstructor thumbnail = new ImageConstructor(thumbnailFile.getName(), is);
		//test by adding 2 product specific image input stream and add to a list
		File productImg1 = new File("C:\\Users\\SAO\\Desktop\\Dinoland pics\\black-pearl.png");
		InputStream is1 = new FileInputStream(productImg1);
		File productImg2 = new File("C:\\Users\\SAO\\Desktop\\Dinoland pics\\white-pearl.png");
		InputStream is2 = new FileInputStream(productImg2);
		List<ImageConstructor> productImgList = new ArrayList<ImageConstructor>();
		productImgList.add(new ImageConstructor(productImg1.getName(), is1));
		productImgList.add(new ImageConstructor(productImg2.getName(), is2));
		//Verify by modifying the products
		ProductExecution pe = productService.modifyProduct(product, thumbnail, productImgList);
		assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());
	}
}
