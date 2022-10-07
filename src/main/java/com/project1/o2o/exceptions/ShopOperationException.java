package com.project1.o2o.exceptions;
// encapsulate RuntimeException to be specific for shop operations

public class ShopOperationException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1031630666954359535L;

	public ShopOperationException(String msg) {
		super(msg);
	}

}
