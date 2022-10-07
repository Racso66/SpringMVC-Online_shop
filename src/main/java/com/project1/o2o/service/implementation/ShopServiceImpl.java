package com.project1.o2o.service.implementation;

import java.io.InputStream;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project1.o2o.dao.ShopDao;
import com.project1.o2o.dto.ShopExecution;
import com.project1.o2o.entity.Shop;
import com.project1.o2o.enums.ShopStateEnum;
import com.project1.o2o.exceptions.ShopOperationException;
import com.project1.o2o.service.ShopService;
import com.project1.o2o.util.ImageUtil;
import com.project1.o2o.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService{
	@Autowired
	private ShopDao shopDao;
	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException{
		// Check validity of parameters passed in, area, category..
		if(shop==null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		
		try {
			// Set initial values for shop info
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEdited(new Date());
			// add shop info
			int effectedNum = shopDao.insertShop(shop);
			if(effectedNum <= 0) { //runtime exception will cause transaction to terminate and roll back
				throw new ShopOperationException("Failed to create shop");
			} else {
				if(shopImgInputStream != null) {
					//store img
					try {
						addShopImg(shop,shopImgInputStream, fileName);
					} catch (Exception e) {
						throw new ShopOperationException("addShopImg error:" + e.getMessage());
					}
					// update image address
					effectedNum = shopDao.updateShop(shop);
					if(effectedNum <= 0) { 
						throw new ShopOperationException("Failed to update image address");
					}
				}
			}
		} catch(Exception e) {
			throw new ShopOperationException("addShop error:" + e.getMessage());
		}
		return new ShopExecution(ShopStateEnum.CHECK,shop);
	}
	private void addShopImg(Shop shop, InputStream shopImgInputStream, String fileName) {
		// acquire shopimg relative address
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(shopImgInputStream, fileName, dest);
		shop.setShopImg(shopImgAddr);
	}

}