package com.project1.o2o.enums;

public enum ShopStateEnum {
	CHECK(0, "Verifying"), OFFLINE(-1, "Illegal shop"), SUCCESS(1, "Operation successful"), PASS(2, "Qualified"),
	INNER_ERROR(-1001, "System Error"), NULL_SHOPID(-1002, "No shop id"), NULL_SHOP(-1003, "No shop info");

	private int state;
	private String stateInfo;

	private ShopStateEnum(int state, String stateInfo) { // set to private untouched by other program
		this.state = state;
		this.stateInfo = stateInfo;
	}

	/**
	 * return enum value according to status
	 */
	public static ShopStateEnum stateOf(int state) {
		for (ShopStateEnum stateEnum : values()) {
			if (stateEnum.getState() == state) {
				return stateEnum;
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
