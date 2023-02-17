package com.project1.o2o.service.implementation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project1.o2o.cache.JedisUtil;
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
	@Autowired
	private JedisUtil.Keys jedisKeys;
	@Autowired
	private JedisUtil.Strings jedisStrings;
	
	private static Logger logger = LoggerFactory.getLogger(ShopCategoryService.class);

	@Override
	public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
		String key = SCLISTKEY;
		List<ShopCategory> shopCategoryList = null;
		ObjectMapper mapper = new ObjectMapper();
		// concat key for redis
		if(shopCategoryCondition == null)
			key = key + "_allfirstleve"; //parent Id == null if search condition is empty
		else if(shopCategoryCondition != null && shopCategoryCondition.getParent() != null &&
					shopCategoryCondition.getParent().getShopCategoryId() != null)
			key = key + "_parent" + shopCategoryCondition.getParent().getShopCategoryId();
		else if(shopCategoryCondition != null)
			key = key + "_allsecondlevel";//list all child categories, no matter the parent
		
		if(!jedisKeys.exists(key)) {
			// get info from db
			shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
			// store info into String format
			String jsonString;
			try {
				jsonString = mapper.writeValueAsString(shopCategoryList);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			}
			jedisStrings.set(key, jsonString);
		} else {
			String jsonString = jedisStrings.get(key);
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
			try {
				shopCategoryList = mapper.readValue(jsonString, javaType);
			} catch (JsonMappingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			}
		}
		return shopCategoryList;
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
