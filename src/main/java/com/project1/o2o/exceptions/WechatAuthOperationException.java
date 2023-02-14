package com.project1.o2o.exceptions;

public class WechatAuthOperationException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7992820836348557782L;
	
	public WechatAuthOperationException(String msg) {
		super(msg);
	}
}
