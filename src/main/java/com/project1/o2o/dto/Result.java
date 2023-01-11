package com.project1.o2o.dto;

/*  
 * Packages Json target, used for all return values 
 */
public class Result<T> {
	private boolean success; //Determine success or not
	
	private T data;//data returned when success
	
	private String errorMsg;
	
	private int errorCode;
	
	public Result() {
	}
	
	// Constructor for success
	public Result(boolean success, T data) {
		this.success = success;
		this.data = data;
	}
	
	// Constructor for error
	public Result(boolean success, int errorCode, String errorMsg) {
		this.success = success;
		this.errorMsg = errorMsg;
		this.errorCode = errorCode;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
