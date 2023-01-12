package com.project1.o2o.enums;

public enum ProductStateEnum {
	OFFLINE(-1, "Illegal product"), DOWN(0, "Discontinued"), SUCCESS(1, "Operation successful"), INNER_ERROR(-1001, "System Error"), EMPTY(-1002, "Empty product info");

	private int state;

	private String stateInfo;

	private ProductStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static ProductStateEnum stateOf(int index) {
		for (ProductStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}
}
