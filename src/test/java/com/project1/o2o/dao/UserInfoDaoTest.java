package com.project1.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.project1.o2o.BaseTest;
import com.project1.o2o.entity.UserInfo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserInfoDaoTest extends BaseTest{
	@Autowired
	private UserInfoDao userInfoDao;
	
	@Test
	public void testAInsertPersonInfo() throws Exception{
		UserInfo ui = new UserInfo();
		ui.setName("Test Name");
		ui.setGender("Female");
		ui.setUserType(1);//customer
		ui.setCreateTime(new Date());
		ui.setLastEdited(new Date());
		ui.setEnableStatus(1);
		int effectedNum = userInfoDao.insertUserInfo(ui);
		assertEquals(1, effectedNum);
	}
	
	@Test
	public void testBQueryUserInfoById() {
		long userId = 1;
		UserInfo user = userInfoDao.queryUserInfoById(userId);
		System.out.println(user.getUserName());
	}
}
