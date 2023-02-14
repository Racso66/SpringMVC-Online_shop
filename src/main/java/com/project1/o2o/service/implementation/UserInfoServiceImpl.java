package com.project1.o2o.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project1.o2o.dao.UserInfoDao;
import com.project1.o2o.entity.UserInfo;
import com.project1.o2o.service.UserInfoService;

@Service
public class UserInfoServiceImpl implements UserInfoService{
	@Autowired
	private UserInfoDao userInfoDao;
	
	@Override
	public UserInfo getUserInfoById(Long userId) {
		return userInfoDao.queryUserInfoById(userId);
	}

}
