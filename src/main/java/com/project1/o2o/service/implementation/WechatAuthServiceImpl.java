package com.project1.o2o.service.implementation;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project1.o2o.dao.UserInfoDao;
import com.project1.o2o.dao.WechatAuthDao;
import com.project1.o2o.dto.WechatAuthExecution;
import com.project1.o2o.entity.UserInfo;
import com.project1.o2o.entity.WechatAuth;
import com.project1.o2o.enums.WechatAuthStateEnum;
import com.project1.o2o.exceptions.WechatAuthOperationException;
import com.project1.o2o.service.WechatAuthService;

@Service
public class WechatAuthServiceImpl implements WechatAuthService{
	private static Logger log = LoggerFactory.getLogger(WechatAuthService.class);
	@Autowired
	private WechatAuthDao wechatAuthDao;
	@Autowired
	private UserInfoDao userInfoDao;
	
	@Override
	public WechatAuth getWechatAuthByOpenId(String openId) {
		//return wechat auth with user info(since they are connected by userId)
		return wechatAuthDao.queryWechatInfoByOpenId(openId);
	}

	@Override
	@Transactional
	public WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException {
		//must exist wechatAuth input to register
		if(wechatAuth == null || wechatAuth.getOpenId() == null) 
			return new WechatAuthExecution(WechatAuthStateEnum.NULL_AUTH_INFO);
		try {
			wechatAuth.setCreateTime(new Date());
			//if there are user info in wechat auth but userId is null, assume first time user(through wechat)
			//Create UserInfo for the wechat user
			if(wechatAuth.getUserInfo() != null && wechatAuth.getUserInfo().getUserId() == null) {
				try {
					wechatAuth.getUserInfo().setCreateTime(new Date());
					wechatAuth.getUserInfo().setEnableStatus(1);//1 is usable status
					UserInfo userInfo = wechatAuth.getUserInfo();
					int effectedNum = userInfoDao.insertUserInfo(userInfo);//useGeneratedKey gives UserId
					wechatAuth.setUserInfo(userInfo);
					if(effectedNum < 1) {
						throw new WechatAuthOperationException("Failed to add user information");
					}
				} catch(Exception e) {
					log.error("error on insertUserInfo: " + e.toString());
					throw new WechatAuthOperationException("Error on insertUserInfo: " + e.getMessage());
				}
			}
			//Create WechatAuth account belonging to the platform (UserId is not null)
			int effectedNum = wechatAuthDao.insertWechatAuth(wechatAuth);
			if(effectedNum < 1) throw new WechatAuthOperationException("Failed to create account");
			else return new WechatAuthExecution(WechatAuthStateEnum.SUCCESS, wechatAuth);
		} catch(Exception e) {
			log.error("error on insertWechatAuth: " + e.toString());
			throw new WechatAuthOperationException("Error on insertWechatAuth: " + e.getMessage());
		}
	}
	
}
