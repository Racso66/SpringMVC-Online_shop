package com.project1.o2o.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project1.o2o.dao.AreaDao;
import com.project1.o2o.entity.Area;
import com.project1.o2o.service.AreaService;

@Service
public class AreaServiceImpl implements AreaService {
	@Autowired
	private AreaDao areaDao;

	@Override
	public List<Area> getAreaList() {
		// return areaDao.queryArea when getAreaList() is returned to retrieve area from
		// idbc
		return areaDao.queryArea();
	} // implement interface, extends class

}
