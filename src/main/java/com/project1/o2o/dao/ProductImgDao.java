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
}
