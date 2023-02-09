package com.project1.o2o.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WechatUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1743743655268530691L;
	
	//The unique identification of the user under the official account
	@JsonProperty("openid")
	private String openId;
	@JsonProperty("nickname")
	private String nickName;
	@JsonProperty("sex")
	private String sex;
	@JsonProperty("country")
	private String country;
	@JsonProperty("province")
	private String province;
	@JsonProperty("city")
	private String city;
	@JsonProperty("headimgurl")
	private String headimgurl;
	@JsonProperty("language")
	private String language;
	@JsonProperty("privilege")
	private String[] privilege;
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String[] getPrivilege() {
		return privilege;
	}
	public void setPrivilege(String[] privilege) {
		this.privilege = privilege;
	}
}
