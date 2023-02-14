package com.project1.o2o.enums;

public enum WechatAuthStateEnum {
	LOGINFAIL(-1, "wrong openId"), SUCCESS(0, "Success"), NULL_AUTH_INFO(-1006, "No Authorization Information");
	private int state;
	private String stateInfo;
	
	private WechatAuthStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	
	public static WechatAuthStateEnum stateOf(int index) {
		for (WechatAuthStateEnum state : values()) {
			return state;
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
