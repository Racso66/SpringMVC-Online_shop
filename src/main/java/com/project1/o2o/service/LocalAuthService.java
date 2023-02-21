package com.project1.o2o.service;

import com.project1.o2o.dto.LocalAuthExecution;
import com.project1.o2o.entity.LocalAuth;
import com.project1.o2o.exceptions.LocalAuthOperationException;

public interface LocalAuthService {
	/*
	 * platform aacount info through password and username
	 */
	LocalAuth getLocalAuthByUserNameAndPwd(String username, String	password);
	
	/*
	 * platform account info through user Id
	 */
	LocalAuth getLocalAuthByUserId(long userId);
	
	/*
	 * Link wechat to platform account
	 */
	LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException;
	
	/*
	 * Change platform account password
	 */
	LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword)
			throws LocalAuthOperationException;
}
