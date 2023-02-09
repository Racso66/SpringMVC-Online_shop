package com.project1.o2o.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAccessToken {
	//certificate
	@JsonProperty("access_token")
	private String accessToken;
	//certificate valid period
	@JsonProperty("expires_in")
	private String expiresIn;
	//to retrieve the next access token
	@JsonProperty("refresh_token")
	private String refreshTokenl;
	//the identification of the user under the official account, unique to wechat account
	@JsonProperty("openid")
	private String openId;
	//scope of privilege
	@JsonProperty("scope")
	private String scope;
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}
	public String getRefreshTokenl() {
		return refreshTokenl;
	}
	public void setRefreshTokenl(String refreshTokenl) {
		this.refreshTokenl = refreshTokenl;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
}
