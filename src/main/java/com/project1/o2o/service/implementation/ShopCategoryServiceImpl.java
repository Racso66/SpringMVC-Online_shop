package com.project1.o2o.service.implementation;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project1.o2o.dao.ShopCategoryDao;
import com.project1.o2o.dto.ImageConstructor;
import com.project1.o2o.dto.ShopCategoryExecution;
import com.project1.o2o.entity.ShopCategory;
import com.project1.o2o.enums.ShopCategoryStateEnum;
import com.project1.o2o.exceptions.ShopCategoryOperationException;
import com.project1.o2o.service.ShopCategoryService;
import com.project1.o2o.util.ImageUtil;
import com.project1.o2o.util.PathUtil;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService{
	@Autowired
	private ShopCategoryDao shopCategoryDao;

	@Override
	public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
		return shopCategoryDao.queryShopCategory(shopCategoryCondition);
	}
	
	@Override
	@Transactional
	public ShopCategoryExecution addShopCategory(ShopCategory shopCategory, ImageConstructor thumbnail) {
		if (shopCategory != null) {
			//set default
			shopCategory.setCreateTime(new Date());
			shopCategory.setLastEdited(new Date());
			if (thumbnail != null) {
				//if there are image file streams, store and set its path to shopcategory class
				addThumbnail(shopCategory, thumbnail);
			}
			try {
				// add shopcategory information to db
				int effectedNum = shopCategoryDao.insertShopCategory(shopCategory);
				if (effectedNum > 0) {
					return new ShopCategoryExecution(ShopCategoryStateEnum.SUCCESS, shopCategory);
				} else {
					return new ShopCategoryExecution(ShopCategoryStateEnum.INNER_ERROR);
				}
			} catch (Exception e) {
				throw new ShopCategoryOperationException("Failed to add shop category: " + e.toString());
			}
		} else {
			return new ShopCategoryExecution(ShopCategoryStateEnum.EMPTY);
		}
	}

	private void addThumbnail(ShopCategory shopCategory, ImageConstructor thumbnail) {
		String dest = PathUtil.getShopCategoryPath();
		String thumbnailAddr = ImageUtil.generateNormalImg(thumbnail, dest);
		shopCategory.setShopCategoryImg(thumbnailAddr);
	}
	
	@Override
	@Transactional
	public ShopCategoryExecution modifyShopCategory(ShopCategory shopCategory, ImageConstructor thumbnail) {
		// focus on shopCategoryId not null
		if (shopCategory.getShopCategoryId() != null && shopCategory.getShopCategoryId() > 0) {
			//set default value
			shopCategory.setLastEdited(new Date());
			if (thumbnail != null) {
				// if there is thumbnail uploaded, retrieve path
				ShopCategory tempShopCategory = shopCategoryDao.queryShopCategoryById(shopCategory.getShopCategoryId());
				if (tempShopCategory.getShopCategoryImg() != null) {
					//if there was already an image, delete it
					ImageUtil.deleteFileOrPath(tempShopCategory.getShopCategoryImg());
				}
				//store new image
				addThumbnail(shopCategory, thumbnail);
			}
			try {
				//update db info
				int effectedNum = shopCategoryDao.updateShopCategory(shopCategory);
				if (effectedNum > 0) {
					return new ShopCategoryExecution(ShopCategoryStateEnum.SUCCESS, shopCategory);
				} else {
					return new ShopCategoryExecution(ShopCategoryStateEnum.INNER_ERROR);
				}
			} catch (Exception e) {
				throw new ShopCategoryOperationException("Failed to update shop category information: " + e.toString());
			}
		} else {
			return new ShopCategoryExecution(ShopCategoryStateEnum.EMPTY);
		}
	}
	
	@Override
	public ShopCategory getShopCategoryById(Long shopCategoryId) {
		return shopCategoryDao.queryShopCategoryById(shopCategoryId);
	}
}
