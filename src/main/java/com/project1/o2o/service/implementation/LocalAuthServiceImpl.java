package com.project1.o2o.service.implementation;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project1.o2o.dao.LocalAuthDao;
import com.project1.o2o.dto.LocalAuthExecution;
import com.project1.o2o.entity.LocalAuth;
import com.project1.o2o.enums.LocalAuthStateEnum;
import com.project1.o2o.exceptions.LocalAuthOperationException;
import com.project1.o2o.service.LocalAuthService;
import com.project1.o2o.util.MD5;

@Service
public class LocalAuthServiceImpl implements LocalAuthService{
	@Autowired
	private LocalAuthDao localAuthDao;
	
	@Override
	public LocalAuth getLocalAuthByUserNameAndPwd(String username, String password) {
		return localAuthDao.queryLocalByUserNameAndPwd(username, MD5.getMd5(password));
	}

	@Override
	public LocalAuth getLocalAuthByUserId(long userId) {
		return localAuthDao.queryLocalByUserId(userId);
	}

	@Override
	@Transactional
	public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
		// check localAuth info received not empty
		if(localAuth == null || localAuth.getPassword() == null || localAuth.getUsername() == null ||
				localAuth.getUserInfo() == null || localAuth.getUserInfo().getUserId() == null) {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
		// Check if user already has Local auth account
		LocalAuth tempAuth = localAuthDao.queryLocalByUserId(localAuth.getUserInfo().getUserId());
		if(tempAuth != null)
			return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
		try {
			// if not binded, create account for the user
			localAuth.setCreateTime(new Date());
			localAuth.setLastEdited(new Date());
			// MD5 hashing
			localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
			int effectedNum = localAuthDao.insertLocalAuth(localAuth);
			if(effectedNum < 1) {
				throw new LocalAuthOperationException("Failed to bind account");
			} else {
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS, localAuth);
			}
		} catch(Exception e) {
			throw new LocalAuthOperationException("insertLocalAuth service failure: " + e.getMessage());
		}
	}

	@Override
	@Transactional
	public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword)
			throws LocalAuthOperationException {
		// check auth info entered not null and old password != new password (nothing is changed)
		if(userId != null && username != null && password != null && newPassword != null && !password.equals(newPassword)) {
			try {
				// Update password and encode with md5 hashing
				int effectedNum = localAuthDao.updateLocalAuth(userId, username, MD5.getMd5(password), MD5.getMd5(newPassword), 
						new Date());
					// Determine if operation was unsuccessful
					if(effectedNum < 1) {
						throw new LocalAuthOperationException("Failed to update password");
					}
					return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
			} catch(Exception e) {
				throw new LocalAuthOperationException("Failed to update password: " + e.getMessage());
			}
		} else {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
	}
}
