package com.project1.o2o.dao;

import com.project1.o2o.entity.UserInfo;

public interface UserInfoDao {
	/*
	 * Search for user through userId
	 */
	UserInfo queryUserInfoById(long userId);
	
	/*
	 * add user info
	 */
	int insertUserInfo(UserInfo userInfo);
}
