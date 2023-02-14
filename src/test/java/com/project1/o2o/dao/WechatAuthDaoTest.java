package com.project1.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.project1.o2o.BaseTest;
import com.project1.o2o.entity.UserInfo;
import com.project1.o2o.entity.WechatAuth;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WechatAuthDaoTest extends BaseTest {
	@Autowired
	private WechatAuthDao wechatAuthDao;

	@Test
	@Ignore
	public void testAInsertWechatAuth() throws Exception {
		// Create new wechat acc
		WechatAuth wechatAuth = new WechatAuth();
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(1L);
		wechatAuth.setUserInfo(userInfo);
		wechatAuth.setOpenId("TestOpenId");
		wechatAuth.setCreateTime(new Date());
		int effectedNum = wechatAuthDao.insertWechatAuth(wechatAuth);
		assertEquals(1, effectedNum);
	}

	@Test
	public void testBQueryWechatAuthByOpenId() throws Exception {
		WechatAuth wechatAuth = wechatAuthDao.queryWechatInfoByOpenId("TestOpenId");
		assertEquals("test", wechatAuth.getUserInfo().getUserName());
	}

}