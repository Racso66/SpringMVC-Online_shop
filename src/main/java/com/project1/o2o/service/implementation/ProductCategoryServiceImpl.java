package com.project1.o2o.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project1.o2o.dao.ProductCategoryDao;
import com.project1.o2o.dto.ProductCategoryExecution;
import com.project1.o2o.entity.ProductCategory;
import com.project1.o2o.enums.ProductCategoryStateEnum;
import com.project1.o2o.exceptions.ProductCategoryOperationException;
import com.project1.o2o.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService{
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Override
	public List<ProductCategory>getProductCategoryList(long shopId) {
		return productCategoryDao.queryProductCategoryList(shopId);
	}

	@Override
	public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException {
		if(productCategoryList != null && productCategoryList.size() > 0) {
			try {
				int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
				if(effectedNum <= 0) {//fails if nothing is effected after insert
					throw new ProductCategoryOperationException("Failed to create product category");
				} else return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);

			} catch (Exception e) {
				throw new ProductCategoryOperationException("batchAddProductCategory error: " + e.getMessage());
			}
		} else return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
	}
	
	@Override
	@Transactional //wait until second step success before executing. Ensures both process submitted successfully
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException{
		//TODO reset the product category id of the products under the category
		try {
			int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if(effectedNum <= 0) throw new ProductCategoryOperationException("failed to delete product category");
			else return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
		} catch(Exception e) {
			throw new ProductCategoryOperationException("deleteProductCategory error: " + e.getMessage());
		}
	}
}
