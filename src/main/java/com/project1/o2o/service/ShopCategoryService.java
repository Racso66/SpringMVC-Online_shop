package com.project1.o2o.service;

import java.util.List;

import com.project1.o2o.entity.ShopCategory;

public interface ShopCategoryService {
		List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);

}
