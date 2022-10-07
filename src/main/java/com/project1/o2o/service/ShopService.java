package com.project1.o2o.service;

import java.io.InputStream;

import com.project1.o2o.dto.ShopExecution;
import com.project1.o2o.entity.Shop;
import com.project1.o2o.exceptions.ShopOperationException;

public interface ShopService {
	ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException;
}
