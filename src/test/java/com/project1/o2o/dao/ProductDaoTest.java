package com.project1.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.project1.o2o.BaseTest;
import com.project1.o2o.entity.Product;
import com.project1.o2o.entity.ProductCategory;
import com.project1.o2o.entity.Shop;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductDaoTest extends BaseTest {
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;

	@Test
	public void testAInsertProduct() throws Exception {
		Shop shop1 = new Shop();
		shop1.setShopId(2L);
		ProductCategory pc1 = new ProductCategory();
		pc1.setProductCategoryId(2L);
		//Initialize 3 product and add into shop with id 1 and category id 1
		Product product1 = new Product();
		product1.setProductName("test 1");
		product1.setProductDesc("test desc 1");
		product1.setImgAddr("test 1");
		product1.setPriority(1);
		product1.setEnableStatus(1);
		product1.setCreateTime(new Date());
		product1.setLastEdited(new Date());
		product1.setShop(shop1);
		product1.setProductCategory(pc1);
		Product product2 = new Product();
		product2.setProductName("test 2");
		product2.setProductDesc("test desc 2");
		product2.setImgAddr("test 2");
		product2.setPriority(2);
		product2.setEnableStatus(0);
		product2.setCreateTime(new Date());
		product2.setLastEdited(new Date());
		product2.setShop(shop1);
		product2.setProductCategory(pc1);
		Product product3 = new Product();
		product3.setProductName("test 3");
		product3.setProductDesc("test desc 3");
		product3.setImgAddr("test 3");
		product3.setPriority(3);
		product3.setEnableStatus(1);
		product3.setCreateTime(new Date());
		product3.setLastEdited(new Date());
		product3.setShop(shop1);
		product3.setProductCategory(pc1);
		
		int effectedNum = productDao.insertProduct(product1);
		assertEquals(1, effectedNum);
		effectedNum = productDao.insertProduct(product2);
		assertEquals(1, effectedNum);
		effectedNum = productDao.insertProduct(product3);
		assertEquals(1, effectedNum);
	}
}
