package com.project1.o2o.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.project1.o2o.BaseTest;
import com.project1.o2o.dto.WechatAuthExecution;
import com.project1.o2o.entity.UserInfo;
import com.project1.o2o.entity.WechatAuth;
import com.project1.o2o.enums.WechatAuthStateEnum;

public class WechatAuthServiceTest extends BaseTest{
	@Autowired
	private WechatAuthService wechatAuthService;
	
	@Test
	public void testRegister() {
		//new wechat auth account
		WechatAuth wechatAuth = new WechatAuth();
		UserInfo ui = new UserInfo();
		String openId = "Register openId";
		//Purposely leave userId emtpy to test Add userInfo when registering wechatauth account
		ui.setCreateTime(new Date());
		ui.setName("Just testing");
		ui.setUserType(1);//customer
		wechatAuth.setUserInfo(ui);
		wechatAuth.setOpenId(openId);
		wechatAuth.setCreateTime(new Date());
		WechatAuthExecution wae = wechatAuthService.register(wechatAuth);
		assertEquals(WechatAuthStateEnum.SUCCESS.getState(), wae.getState());
		//search for newly added wechat auth account through openId
		wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
		//print user_name to verify
		System.out.println(wechatAuth.getUserInfo().getUserName());
	}

}
