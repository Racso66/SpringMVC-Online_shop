package com.project1.o2o.service;

import com.project1.o2o.dto.WechatAuthExecution;
import com.project1.o2o.entity.WechatAuth;
import com.project1.o2o.exceptions.WechatAuthOperationException;

public interface WechatAuthService {
	/*
	 * Using OpenId to find the according wechat account in the platform
	 */
	WechatAuth getWechatAuthByOpenId(String openId);
	
	/**
	 * Register wechat account on the platform
	 * @param wechatAuth
	 * @return
	 * @throws WechatAuthOperationException
	 */
	WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException;
}
