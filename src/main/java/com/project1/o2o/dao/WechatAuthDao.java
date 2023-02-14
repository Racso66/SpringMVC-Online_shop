package com.project1.o2o.dao;

import com.project1.o2o.entity.WechatAuth;

public interface WechatAuthDao {
	/*
	 * find the according wechat account by using openId
	 */
	WechatAuth queryWechatInfoByOpenId(String openId);
	/*
	 * add wechat account according to the system
	 */
	int insertWechatAuth(WechatAuth wechatAuth);
}
