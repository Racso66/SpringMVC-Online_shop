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
import com.project1.o2o.entity.ProductImg;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductImgDaoTest extends BaseTest {
	@Autowired
	private ProductImgDao productImgDao;
	
	@Test
	@Ignore
	public void testABatchInsertProductImg() throws Exception{
		ProductImg productImg1 = new ProductImg();
		productImg1.setImgAddr("image 1");
		productImg1.setImgDesc("test image 1");
		productImg1.setPriority(1);
		productImg1.setCreateTime(new Date());
		productImg1.setProductId(2L);
		ProductImg productImg2 = new ProductImg();
		productImg2.setImgAddr("image 2");
		productImg2.setPriority(1);
		productImg2.setCreateTime(new Date());
		productImg2.setProductId(2L);
		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		productImgList.add(productImg1);
		productImgList.add(productImg2);
		int effectedNum = productImgDao.batchInsertProductImg(productImgList);
		assertEquals(2, effectedNum);
	}
	
	@Test
	public void testBQueryProductImgList() {
		// check productId 1 is it hold two images
		List<ProductImg> productImgList = productImgDao.queryProductImgList(2L);
		assertEquals(2, productImgList.size());
	}
	
	@Test
	@Ignore
	public void testCDeleteProductImgByProductId() throws Exception {
		long productId = 2;
		int effectedNum = productImgDao.deleteProductImgByProductId(productId);
		assertEquals(2, effectedNum);
	}
}
