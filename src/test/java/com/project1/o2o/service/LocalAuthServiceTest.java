package com.project1.o2o.service;

import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.project1.o2o.BaseTest;
import com.project1.o2o.dto.LocalAuthExecution;
import com.project1.o2o.entity.LocalAuth;
import com.project1.o2o.entity.UserInfo;
import com.project1.o2o.enums.WechatAuthStateEnum;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocalAuthServiceTest extends BaseTest{
	@Autowired
	private LocalAuthService localAuthService;
	
	@Test
	@Ignore
	public void testABindLocalAuth() {
		// new platform (local auth) account
		LocalAuth localAuth = new LocalAuth();
		UserInfo userInfo = new UserInfo();
		String username = "testusername";
		String password = "testpassword";
		
		userInfo.setUserId(1L);
		localAuth.setUserInfo(userInfo);
		localAuth.setUsername(username);
		localAuth.setPassword(password);
		
		LocalAuthExecution localAuthExecution = localAuthService.bindLocalAuth(localAuth);
		assertEquals(WechatAuthStateEnum.SUCCESS.getState(), localAuthExecution.getState());
		//get newly added local auth acc through userId
		localAuth = localAuthService.getLocalAuthByUserId(userInfo.getUserId());
		//print username and password to check
		System.out.println("User name: " + localAuth.getUserInfo().getUserName());
		System.out.println("Local auth password: " + localAuth.getPassword());
	}
	
	@Test
	public void testBModifyLocalAuth() {
		long userId = 1;
		String username = "testusername";
		String password = "testpassword";
		String newPassword = "newtestpassword";
		//change password
		LocalAuthExecution localAuthExecution = localAuthService.modifyLocalAuth(userId, username, password, newPassword);
		assertEquals(WechatAuthStateEnum.SUCCESS.getState(), localAuthExecution.getState());
		// find the local auth account using username and new password
		LocalAuth localAuth = localAuthService.getLocalAuthByUserNameAndPwd(username, newPassword);
		System.out.println(localAuth.getUserInfo().getUserName());
	}
}
