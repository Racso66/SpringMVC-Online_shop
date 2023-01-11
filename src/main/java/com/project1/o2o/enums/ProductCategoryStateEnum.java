package com.project1.o2o.enums;

public enum ProductCategoryStateEnum {
	SUCCESS(1, "Success"), INNER_ERROR(-1001, "System error"), EMPTY_LIST(-1002, "List size < 1");

	private int state;
	private String stateInfo;

	private ProductCategoryStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	
	/**
	 * return enum value according to status
	 */
	public static ProductCategoryStateEnum stateOf(int index) {
		for (ProductCategoryStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}
	
	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}
}