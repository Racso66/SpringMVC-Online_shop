package com.project1.o2o.exceptions;
// encapsulate RuntimeException to be specific for shop operations

public class ProductCategoryOperationException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1675308544615167508L;

	public ProductCategoryOperationException(String msg) {
		super(msg);
	}

}

