package com.project1.o2o.web.wechat;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.project1.o2o.dto.UserAccessToken;
import com.project1.o2o.dto.WechatAuthExecution;
import com.project1.o2o.dto.WechatUser;
import com.project1.o2o.entity.UserInfo;
import com.project1.o2o.entity.WechatAuth;
import com.project1.o2o.enums.WechatAuthStateEnum;
import com.project1.o2o.service.UserInfoService;
import com.project1.o2o.service.WechatAuthService;
import com.project1.o2o.util.wechat.WechatUtil;

/*
 * Retrieve user information interface from users that subscribed to the Official Account
 * (Wechat Official Accounts are like FaceBook pages owned by a brand)
 * 
 * Users visiting the official account website will send a code to the system and the code will 
 * then be retrieved by the open id in access_token to retrieve user information
 * 
 * website from wechat: 
https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx4be87ac46480affe&redirect_uri=http://34.216.198.67/o2o/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
 */
@Controller
@RequestMapping("wechatlogin")
public class WechatLoginController {
	private static Logger log = LoggerFactory.getLogger(WechatLoginController.class);
	private static final String FRONTEND="1", SHOPADMIN = "2";
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private WechatAuthService wechatAuthService;
	
	@RequestMapping(value= "/logincheck", method = {RequestMethod.GET})
	public String doGet(HttpServletRequest request, HttpServletResponse response) {
		log.debug("wechat login get...");
		// retrieve "code" from official account. access_token can be used to get user info with code
		String code = request.getParameter("code");
		// state can be used to send custom info: Front page (1) or shop Admin page (2)
		String roleType = request.getParameter("state");
		log.debug("wechat login code: " + code);
		WechatUser user = null;
		String openId = null;
		WechatAuth wechatAuth = null;
		if(code != null) {
			UserAccessToken token;
			try {
				// get access_token through code
				token = WechatUtil.getUserAccessToken(code);
				log.debug("wechat login token: " + token.toString());
				// get accessToken through token
				String accessToken = token.getAccessToken();
				// get openId through token
				openId = token.getOpenId();
				// get user info through openId and access_token
				user = WechatUtil.getUserInfo(accessToken, openId);
				log.debug("wechat login user: " + user.toString());
				request.getSession().setAttribute("openId", openId);
				/*
				 *  After getting openId, use it to determine from database if the wechat account has an
				 *  wechat auth account on the platform. 
				 */
				wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
			} catch(IOException e) {
				log.error("error in getUserAccessToken or getUserInfo or findByOpenId: " + e.toString());
				e.printStackTrace();
			}
		}
		// If no wechat auth exists with the openId, create the account for the connection between 
		// the website and wechat.
		if(wechatAuth == null) {
			UserInfo userInfo = WechatUtil.getUserInfoFromRequest(user);//no userId
			wechatAuth = new WechatAuth();
			wechatAuth.setOpenId(openId);
			if(SHOPADMIN.equals(roleType)) {
				userInfo.setUserType(2);
			} else {
				userInfo.setUserType(1);
			}
			wechatAuth.setUserInfo(userInfo);
			WechatAuthExecution wae = wechatAuthService.register(wechatAuth);//register gives userId
			if(wae.getState() != WechatAuthStateEnum.SUCCESS.getState()) { //failed to create
				return null;
			} else { // success
				userInfo = userInfoService.getUserInfoById(wechatAuth.getUserInfo().getUserId());
				request.getSession().setAttribute("user", userInfo);// used in system
			}
		}
		// Determine user role (customer or shop owner)
		if(FRONTEND.equals(roleType)) {
			return "frontend/index";
		} else {
			return "shopadmin/shoplist";
		}
	}
}
