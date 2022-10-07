package com.project1.o2o.entity;

import java.util.Date;

public class Area {
	// ID
	private Integer areaId;
	// Name
	private String areaName;
	// hierarchy
	private Integer priority;
	// date created
	private Date createTime;
	// most recent update time
	private Date lastEdited;
	//********************
	// Use Integer and Date because I want default value to be null instead of 0 or something
	//********************
	
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastEdited() {
		return lastEdited;
	}
	public void setLastEdit(Date lastEdited) {
		this.lastEdited = lastEdited;
	}
	
}
