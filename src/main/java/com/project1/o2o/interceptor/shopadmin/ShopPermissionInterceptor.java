package com.project1.o2o.interceptor.shopadmin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.project1.o2o.entity.Shop;

/*
 * Shop admin operation permission interceptor
 */
public class ShopPermissionInterceptor extends HandlerInterceptorAdapter{
	/*
	 * Intercept before user operations.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
		// get current shop from session
		Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
		@SuppressWarnings("unchecked")
		// get the operate-able shop list for the current user
		List<Shop> shopList = (List<Shop>)request.getSession().getAttribute("shopList");
		if(currentShop != null && shopList != null) {
			// treverse operate-able shoplist
			for(Shop shop : shopList) {
				if(shop.getShopId() == currentShop.getShopId())
					return true; //the user owns this shop and has operation permission
			}
		}
		// fails interceptor check, terminate user operation
		return false;
	}
}
