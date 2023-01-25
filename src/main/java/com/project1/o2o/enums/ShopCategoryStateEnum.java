package com.project1.o2o.enums;

public enum ShopCategoryStateEnum {
	SUCCESS(0, "Create success"), INNER_ERROR(-1001, "Operation fail"), EMPTY(-1002, "Empty info");
	
	private int state;
	private String stateInfo;
	
	private ShopCategoryStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	
	public int getState() {
		return state;
	}
	
	public String getStateInfo() {
		return stateInfo;
	}
	
	public static ShopCategoryStateEnum stateOf(int index) {
		for(ShopCategoryStateEnum state : values()) {
			if(state.getState() == index) {
				return state;
			}
		}
		return null;
	}
}
