package com.project1.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.project1.o2o.BaseTest;
import com.project1.o2o.entity.Product;
import com.project1.o2o.entity.ProductCategory;
import com.project1.o2o.entity.ProductImg;
import com.project1.o2o.entity.Shop;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductDaoTest extends BaseTest {
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;

	@Test
	@Ignore
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
		product3.setProductName("product 3");
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
	@Test
	@Ignore
	public void testBQueryProductList() throws Exception{
		Product productCondition = new Product();
		//pagination, expected 3
		List<Product> productList = productDao.queryProductList(productCondition, 0, 3);
		assertEquals(3, productList.size());
		//check entire list (2 already exist)
		int count = productDao.queryProductCount(productCondition);
		assertEquals(5, count);
		//check fuzzy search on "test" keyword for product name
		productCondition.setProductName("test");
		productList = productDao.queryProductList(productCondition, 0, 3);
		assertEquals(2, productList.size());
		count = productDao.queryProductCount(productCondition);
		assertEquals(2, count);
	}
	
	@Test
	@Ignore
	public void testCQueryProductByProductId() throws Exception{
		long productId = 2;
		ProductImg productImg1 = new ProductImg();
		productImg1.setImgAddr("Image 1");
		productImg1.setImgDesc("Test product1");
		productImg1.setPriority(1);
		productImg1.setCreateTime(new Date());
		productImg1.setProductId(productId);
		ProductImg productImg2 = new ProductImg();
		productImg2.setImgAddr("Image 2");
		productImg2.setImgDesc("Test product2");
		productImg2.setPriority(1);
		productImg2.setCreateTime(new Date());
		productImg2.setProductId(productId);
		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		productImgList.add(productImg2);
		productImgList.add(productImg1);
		//add to list
		int effectedNum = productImgDao.batchInsertProductImg(productImgList);
		assertEquals(2, effectedNum);
		//check size of list
		Product product = productDao.queryByProductId(productId);
		assertEquals(2, product.getProductImgList().size());
		//delete from list
		effectedNum = productImgDao.deleteProductImgByProductId(productId);
		assertEquals(2, effectedNum);
	}
	
	@Test
	@Ignore
	public void testDUpdateProduct() throws Exception{
		Product product = new Product();
		ProductCategory pc = new ProductCategory();
		Shop shop = new Shop();
		shop.setShopId(2L);
		pc.setProductCategoryId(2L);
		product.setProductId(2L);
		product.setShop(shop);
		product.setProductName("first product");
		product.setProductCategory(pc);
		int effectedNum = productDao.updateProduct(product);
		assertEquals(1, effectedNum);
	}
	
	@Test
	public void testEUpdateProductCategoryToNull() {
		int effectedNum = productDao.updateProductCategoryToNull(3L);
		assertEquals(1, effectedNum);
	}
}
