package com.project1.o2o.service.implementation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project1.o2o.dao.ProductDao;
import com.project1.o2o.dao.ProductImgDao;
import com.project1.o2o.dto.ImageConstructor;
import com.project1.o2o.dto.ProductExecution;
import com.project1.o2o.entity.Product;
import com.project1.o2o.entity.ProductImg;
import com.project1.o2o.enums.ProductStateEnum;
import com.project1.o2o.exceptions.ProductOperationException;
import com.project1.o2o.service.ProductService;
import com.project1.o2o.util.ImageUtil;
import com.project1.o2o.util.PathUtil;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;
	
	/* STEPS OF ADD PRODUCT
	 * 1. Process thumbnail images, acquire relative path and store into product.\
	 * 2. Write product information into database tb_product, then retrieve productId
	 * 3. With productId, batch process product specific images received from front-end
	 * 4. Store product specific images into database tb_product_img
	 */
	@Override
	@Transactional //ensure all 4 steps are executed, roll back if any step fails
	public ProductExecution addProduct(Product product, ImageConstructor thumbnail, List<ImageConstructor> productImgCtorList) throws ProductOperationException {
		//first check for empty product
		if(product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			//set default values for product
			product.setCreateTime(new Date());
			product.setLastEdited(new Date());
			product.setEnableStatus(1);
			//Add to product image if thumbnail not empty
			if(thumbnail != null) {
				addThumbnail(product,thumbnail);
			}
			try {
				//create product information
				int effectedNum = productDao.insertProduct(product);
				if(effectedNum <= 0) throw new ProductOperationException("Failed to create product");
			} catch (Exception e) {
				throw new ProductOperationException("Failed to create product: " + e.toString());
			}
			// Add to product if product there are specific images
			if(productImgCtorList != null && productImgCtorList.size() > 0) addProductImgList(product, productImgCtorList);
			return new ProductExecution(ProductStateEnum.SUCCESS, product);
		}
		else return new ProductExecution(ProductStateEnum.EMPTY);
	}
	
	/**
	 * Batch add product image
	 * @param product
	 * @param productImgList
	 */
	private void addProductImgList(Product product, List<ImageConstructor> productImgCtorList) {
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		//Traverse and process through product images then store into productImg class
		for(ImageConstructor productImgCtor : productImgCtorList) {
			String imgAddr = ImageUtil.generateNormalImg(productImgCtor, dest);
			ProductImg productImg = new ProductImg();
			productImg.setImgAddr(imgAddr);
			productImg.setCreateTime(new Date());
			productImg.setProductId(product.getProductId());
			productImgList.add(productImg);
		}
		//if there are images to be added. execute batch add product image
		if(productImgList.size() > 0) {
			try {
				int effectedNum = productImgDao.batchInsertProductImg(productImgList);
				if(effectedNum <= 0) throw new ProductOperationException("Failed to create product specific image");
			} catch (Exception e) {
				throw new ProductOperationException("Failed to create product specific image: " + e.toString());
			}
		}
	}

	/**
	 * Add thumbnail information into product
	 * @param product
	 * @param thumbnail
	 */
	private void addThumbnail(Product product, ImageConstructor thumbnail) {
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		product.setImgAddr(thumbnailAddr);
	}
}
