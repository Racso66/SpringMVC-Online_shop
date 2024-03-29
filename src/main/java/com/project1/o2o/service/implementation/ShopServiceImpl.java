package com.project1.o2o.service.implementation;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project1.o2o.dao.ShopDao;
import com.project1.o2o.dto.ImageConstructor;
import com.project1.o2o.dto.ShopExecution;
import com.project1.o2o.entity.Shop;
import com.project1.o2o.enums.ShopStateEnum;
import com.project1.o2o.exceptions.ShopOperationException;
import com.project1.o2o.service.ShopService;
import com.project1.o2o.util.ImageUtil;
import com.project1.o2o.util.PageCalculator;
import com.project1.o2o.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService {
	@Autowired
	private ShopDao shopDao;
	
	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		int count = shopDao.queryShopCount(shopCondition);
		ShopExecution se = new ShopExecution();
		if(shopList != null) {
			se.setShopList(shopList);
			se.setCount(count);
		}else {
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
	}
	
	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, ImageConstructor thumbnail)
			throws ShopOperationException {
		// Check validity of parameters passed in, area, category..
		if (shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}

		try {
			// Set initial values for shop info
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEdited(new Date());
			// add shop info
			int effectedNum = shopDao.insertShop(shop);
			if (effectedNum <= 0) { // runtime exception will cause transaction to terminate and roll back
				throw new ShopOperationException("Failed to create shop");
			} else {
				if (thumbnail.getImage() != null) {
					// store img
					try {
						addShopImg(shop, thumbnail);
					} catch (Exception e) {
						throw new ShopOperationException("addShopImg error:" + e.getMessage());
					}
					// update image address
					effectedNum = shopDao.updateShop(shop);
					if (effectedNum <= 0) {
						throw new ShopOperationException("Failed to update image address");
					}
				}
			}
		} catch (Exception e) {
			throw new ShopOperationException("addShop error:" + e.getMessage());
		}
		return new ShopExecution(ShopStateEnum.CHECK, shop);
	}

	private void addShopImg(Shop shop, ImageConstructor thumbnail) {
		// acquire shopimg relative address
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		shop.setShopImg(shopImgAddr);
	}

	@Override
	public Shop getByShopId(long shopId) {
		return shopDao.queryByShopId(shopId);
	}

	@Override
	public ShopExecution modifyShop(Shop shop, ImageConstructor thumbnail)
			throws ShopOperationException {
		if (shop == null || shop.getShopId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		} else {
			// 1. Determine if there is an image to process
			try {
				if (thumbnail.getImage() != null && thumbnail.getImageName() != null && !"".equals(thumbnail.getImageName())) {
					Shop tempShop = shopDao.queryByShopId(shop.getShopId());
					if (tempShop.getShopImg() != null) {
						ImageUtil.deleteFileOrPath(tempShop.getShopImg());
					}
					addShopImg(shop, thumbnail);
				}
				// 2. Update shop information
				shop.setLastEdited(new Date());
				int effectedNum = shopDao.updateShop(shop); // usual get a line of value
				if (effectedNum <= 0) {
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
				} else {
					shop = shopDao.queryByShopId(shop.getShopId());
					return new ShopExecution(ShopStateEnum.SUCCESS, shop);
				}
			} catch (Exception e) {
				throw new ShopOperationException("modifyShop error:" + e.getMessage());
			}
		}
	}

}
