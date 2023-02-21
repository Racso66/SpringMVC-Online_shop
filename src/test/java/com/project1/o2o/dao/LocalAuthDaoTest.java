package com.project1.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.project1.o2o.BaseTest;
import com.project1.o2o.entity.LocalAuth;
import com.project1.o2o.entity.UserInfo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocalAuthDaoTest extends BaseTest {
	@Autowired
	private LocalAuthDao localAuthDao;
	private static final String username = "testusername";
	private static final String password = "testpassword";
	
	@Test
	public void testAInsertLocalAuth() throws Exception {
		LocalAuth localAuth = new LocalAuth();
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(1L);
		localAuth.setUserInfo(userInfo);
		localAuth.setUsername(username);
		localAuth.setPassword(password);
		localAuth.setCreateTime(new Date());
		int effectedNum = localAuthDao.insertLocalAuth(localAuth);
		assertEquals(1, effectedNum);
	}
	
	@Test
	public void testBQueryLocalByUserNameAndPwd() throws Exception {
		LocalAuth localAuth = localAuthDao.queryLocalByUserNameAndPwd(username, password);
		assertEquals("test", localAuth.getUserInfo().getUserName());
	}
	
	@Test
	public void testCQueryLocalByUserId() throws Exception {
		LocalAuth localAuth = localAuthDao.queryLocalByUserId(1L);
		assertEquals("test", localAuth.getUserInfo().getUserName());
	}
	
	@Test
	public void testDUpdateLocalAuth() throws Exception {
		Date currentTime = new Date();
		// change password to "newtestpassword" from "testpassword"
		int effectedNum = localAuthDao.updateLocalAuth(1L, username, password, "new" + password, currentTime);
		assertEquals(1, effectedNum);
		LocalAuth localAuth = localAuthDao.queryLocalByUserId(1L);
		// check if password = "newtestpassword"
		System.out.println(localAuth.getPassword());
		
	}
}
