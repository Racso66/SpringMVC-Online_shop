package com.project1.o2o.web.local;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project1.o2o.dto.LocalAuthExecution;
import com.project1.o2o.entity.LocalAuth;
import com.project1.o2o.entity.UserInfo;
import com.project1.o2o.enums.LocalAuthStateEnum;
import com.project1.o2o.service.LocalAuthService;
import com.project1.o2o.util.CodeUtil;
import com.project1.o2o.util.HttpServletRequestUtil;

/*
 * Login | log out | bind account | change password
 */
@Controller
@RequestMapping(value = "local", method = {RequestMethod.GET, RequestMethod.POST})
public class LocalAuthController {
	@Autowired
	private LocalAuthService localAuthService;
	
	/*
	 * bind user info with platform account
	 */
	@RequestMapping(value = "/bindlocalauth", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> bindLocalAuth(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// kaptcha
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "Wrong verification code entered");
			return modelMap;
		}
		String username = HttpServletRequestUtil.getString(request, "username");
		String password = HttpServletRequestUtil.getString(request, "password");
		//get user info from session, (info received once user log in through wechat)
		UserInfo user = (UserInfo)request.getSession().getAttribute("user");
		// requires username and password and session user not null
		if(username != null && password != null && user != null && user.getUserId() != null) {
			// new localauth with settings
			LocalAuth localAuth = new LocalAuth();
			localAuth.setUsername(username);
			localAuth.setPassword(password);
			localAuth.setUserInfo(user);
			// bind
			LocalAuthExecution localAuthExecution = localAuthService.bindLocalAuth(localAuth);
			if(localAuthExecution.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", localAuthExecution.getStateInfo());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "Password and username are not allowed to be empty");
		}
		return modelMap;
	}
	
	/**
	 * Password change
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/changelocalpwd", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> changeLocalPwd(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
			//kaptcha
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "Wrong verification code entered");
			return modelMap;
		}
		String username = HttpServletRequestUtil.getString(request, "username");
		String password = HttpServletRequestUtil.getString(request, "password");
		String newPassword = HttpServletRequestUtil.getString(request, "newPassword");
		//get user info from session, (info received once user log in through wechat)
		UserInfo user = (UserInfo)request.getSession().getAttribute("user");
		// requires username and password and session user not null, newPassword != old password
		if(username != null && password != null && user != null && user.getUserId() != null &&
				!password.equals(newPassword) && newPassword != null) {
			try {
				LocalAuth localAuth = localAuthService.getLocalAuthByUserId(user.getUserId());
				// check by service if account entered is the account logged in as. Illegal if not.
				if(localAuth == null || !localAuth.getUsername().equals(username)) {
					modelMap.put("success", false);
					modelMap.put("errMsg", "Entered account is not the account used to log in");
					return modelMap;
				}
				// change password
				LocalAuthExecution localAuthExecution = localAuthService.modifyLocalAuth(user.getUserId(), 
						username, password, newPassword);
				if(localAuthExecution.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", localAuthExecution.getStateInfo());
				}
			} catch(Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "Please enter all account information");
		}
		return modelMap;
	}
	
	/*
	 * Set login checks
	 */
	@RequestMapping(value = "/logincheck", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> logincheck(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// decide if login verification needed (when password entered wrong 3+ times)
		boolean needVerify = HttpServletRequestUtil.getBoolean(request, "needVerify");
		if(needVerify && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "Wrong verification code entered");
			return modelMap;
		}
		String username = HttpServletRequestUtil.getString(request, "username");
		String password = HttpServletRequestUtil.getString(request, "password");
		// info not null
		if(username != null && password != null) {
			// get account through username and password
			LocalAuth localAuth = localAuthService.getLocalAuthByUserNameAndPwd(username, password);
			if(localAuth != null) {
				modelMap.put("success", true);
				request.getSession().setAttribute("user", localAuth.getUserInfo());
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "wrong username or password");
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "username and password must be entered");
		}
		return modelMap;
	}
	
	/*
	 * Sign out of current session when user clicks on logout
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> logout(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// Empty user session info
		request.getSession().setAttribute("user", null);
		modelMap.put("success", true);
		return modelMap;
	}
}
