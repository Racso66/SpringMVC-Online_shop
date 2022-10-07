package com.project1.o2o.dto;

import java.util.List;

import com.project1.o2o.entity.Shop;
import com.project1.o2o.enums.ShopStateEnum;

/*
 * Store shop information and status value
 */
public class ShopExecution {
	// result status
	private int state;

	// current status, more information on status
	private String stateInfo;

	// shop count
	private int count;

	// current modifying shop
	private Shop shop;

	// shop list. for checking list of shops
	private List<Shop> shopList;

	public ShopExecution() { // default

	}

	// constructor for target shop when fail, only returns status
	public ShopExecution(ShopStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}

	// constructor for target when success, returns status and shop
	public ShopExecution(ShopStateEnum stateEnum, Shop shop) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.shop = shop;
	}

	// constructor for target when success, constructs the list of shops
	public ShopExecution(ShopStateEnum stateEnum, List<Shop> shopList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.shopList = shopList;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public List<Shop> getShopList() {
		return shopList;
	}

	public void setShopList(List<Shop> shopList) {
		this.shopList = shopList;
	}
}
