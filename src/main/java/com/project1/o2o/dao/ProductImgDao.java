package com.project1.o2o.dao;

import java.util.List;

import com.project1.o2o.entity.ProductImg;

public interface ProductImgDao {
	/**
	 * 
	 * @param productImgList
	 * @return
	 */
	int batchInsertProductImg(List<ProductImg> productImgList);
	
	/**
	 * delete all product specific images under the selected product
	 * @param productId
	 * @return
	 */
	int deleteProductImgByProductId(long productId);
	
	/**
	 * 
	 * 
	 * @param productId
	 * @return
	 */
	List<ProductImg> queryProductImgList(long productId);
}
