package com.project1.o2o.interceptor.shopadmin;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.project1.o2o.entity.UserInfo;

/*
 * Login check interceptor for shop admin. Extend handlerInterceptor interface methods
 * 1. prehandle 2. posthandle 3. aftercompletion
 */
public class ShopLoginInterceptor extends HandlerInterceptorAdapter{
	/**
	 * Intercept before user operations.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
		// info from session
		Object userObject = request.getSession().getAttribute("user");
		if(userObject != null) {
			//convert user info to UserInfo.class
			UserInfo user = (UserInfo)userObject;
			if(user != null && user.getUserId() != null && user.getUserId() > 0 && user.getEnableStatus() == 1)
				// passes login check, user operates normally. return true sends to permission check interceptor
				return true;
		}
		// no user login, redirect to login page
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<script>");
		out.println("window.open('" + request.getContextPath() + "/local/login?usertype=2','_self')");
		out.println("</script>");
		out.println("</html>");
		return false; // stops controller methods
	}
}
