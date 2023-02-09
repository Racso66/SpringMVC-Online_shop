package com.project1.o2o.web.wechat;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.project1.o2o.dto.UserAccessToken;
import com.project1.o2o.dto.WechatUser;
import com.project1.o2o.util.wechat.WechatUtil;

/*
 * Retrieve user information interface from users that subscribed to the Official Account
 * (Wechat Official Accounts are like FaceBook pages owned by a brand)
 * 
 * Users visiting the official account website will send a code to the system and the code will 
 * then be retrieved by the open id in access_token to retrieve user information
 * 
 * website from wechat: https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx4be
 * 						87ac46480affe&redirect_uri=http://34.216.198.67/o2o/wechatlogin/login
 * 						check&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#we
 * 						chat_redirect
 */
@Controller
@RequestMapping("wechatlogin")
public class WechatLoginController {
	private static Logger log = LoggerFactory.getLogger(WechatLoginController.class);
	
	@RequestMapping(value= "/logincheck", method = {RequestMethod.GET})
	public String doGet(HttpServletRequest request, HttpServletResponse response) {
		log.debug("wechat login get...");
		// retrieve "code" from official account. access_token can be used to get user info with code
		String code = request.getParameter("code");
		// state can be used to send custom info: String roleType = request.getParameter("state");
		log.debug("wechat login code: " + code);
		WechatUser user = null;
		String openId = null;
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
			} catch(IOException e) {
				log.error("error in getUserAccessToken or getUserInfo or findByOpenId: " + e.toString());
				e.printStackTrace();
			}
		}
		/*
		 *  TODO: After getting openId, use it to determine from database if the wechat account has an
		 *  account in the website. If not, create the account for the connection between the website
		 *  and wechat.
		 */
		if(user != null) return "frontend/index";
		else return null;
	}
}
