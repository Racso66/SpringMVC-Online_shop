package com.project1.o2o.dto;

import java.util.List;

import com.project1.o2o.entity.ProductCategory;
import com.project1.o2o.enums.ProductCategoryStateEnum;

public class ProductCategoryExecution {
	private int state;

	private String stateInfo;

	private List<ProductCategory> productCategoryList;

	// default constructor
	public ProductCategoryExecution() {

	}

	// Constructor for failed operations
	public ProductCategoryExecution(ProductCategoryStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}

	// Constructor for successful operations
	public ProductCategoryExecution(ProductCategoryStateEnum stateEnum, List<ProductCategory> productCategoryList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.productCategoryList = productCategoryList;
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

	public List<ProductCategory> getProductCategoryList() {
		return productCategoryList;
	}

	public void setProductCategoryList(List<ProductCategory> productCategoryList) {
		this.productCategoryList = productCategoryList;
	}

}
