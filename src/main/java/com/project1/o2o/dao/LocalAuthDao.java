package com.project1.o2o.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.project1.o2o.entity.LocalAuth;

public interface LocalAuthDao {
	/**
	 * Search for local info through user name and password. Used for login
	 * @param username
	 * @param password
	 * @return
	 */
	LocalAuth queryLocalByUserNameAndPwd(@Param("username") String username, @Param("password") String password);
	
	/*
	 * Search for local info through user Id
	 */
	LocalAuth queryLocalByUserId(@Param("userId") long userId);
	
	/*
	 * Add account to platform
	 */
	int insertLocalAuth(LocalAuth localAuth);
	
	/**
	 * Update Local account's password through userId username and password
	 * 
	 * @param userId
	 * @param username
	 * @param password
	 * @param newPassword
	 * @param lastEdited
	 * @return
	 */
	int updateLocalAuth(@Param("userId") Long userId, @Param("username") String username, @Param("password") String password,
			@Param("newPassword") String newPassword, @Param("lastEdited") Date lastEdited);
}
