package com.project1.o2o.service;

import java.util.List;

import com.project1.o2o.entity.Area;

public interface AreaService {
	public static final String AREALISTKEY = "arealist";
	
	List<Area> getAreaList();
}
