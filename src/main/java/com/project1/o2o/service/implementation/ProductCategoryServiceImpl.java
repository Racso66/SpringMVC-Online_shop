package com.project1.o2o.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project1.o2o.dao.ProductCategoryDao;
import com.project1.o2o.entity.ProductCategory;
import com.project1.o2o.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService{
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Override
	public List<ProductCategory>getProductCategoryList(long shopId) {
		return productCategoryDao.queryProductCategoryList(shopId);
	}
}