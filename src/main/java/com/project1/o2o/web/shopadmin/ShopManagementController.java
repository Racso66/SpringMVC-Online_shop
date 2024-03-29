package com.project1.o2o.web.shopadmin;

//import java.io.File;
//import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
//import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project1.o2o.dto.ImageConstructor;
import com.project1.o2o.dto.ShopExecution;
import com.project1.o2o.entity.Area;
import com.project1.o2o.entity.Shop;
import com.project1.o2o.entity.ShopCategory;
import com.project1.o2o.entity.UserInfo;
import com.project1.o2o.enums.ShopStateEnum;
import com.project1.o2o.exceptions.ShopOperationException;
import com.project1.o2o.service.AreaService;
import com.project1.o2o.service.ShopCategoryService;
import com.project1.o2o.service.ShopService;
import com.project1.o2o.util.CodeUtil;
import com.project1.o2o.util.HttpServletRequestUtil;

/*
 * Shop management logic
 * 
 *
 */
@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;
	
	@RequestMapping(value = "/getshopmanagementinfo", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopManagementInfo(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId <= 0) {
			Object currentShopObj = request.getSession().getAttribute("currentShop");
			if(currentShopObj == null) { //If direct visit the site without login or shopList urls, redirect to shopList
				modelMap.put("redirect", true);
				modelMap.put("url", "/o2o/shopadmin/shoplist");
			} else { //if already logged in, no need to redirect
				Shop currentShop = (Shop) currentShopObj;
				modelMap.put("redirect", false);
				modelMap.put("shopId", currentShop.getShopId());
			}
		} else {//if valid shopId is entered, assume power to modify the shop. Set blocker
			Shop currentShop = new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop", currentShop);
			modelMap.put("redirect", false);
		}
		return modelMap;
	}	
	
	@RequestMapping(value = "/getshoplist", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopList(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// Test code
//		UserInfo user = new UserInfo();
//		user.setUserId(1L);
//		user.setName("Test name");
//		request.getSession().setAttribute("user", user);
		UserInfo user = (UserInfo) request.getSession().getAttribute("user");
		try {
			Shop shopCondition = new Shop();
			shopCondition.setOwner(user);
			ShopExecution se = shopService.getShopList(shopCondition, 0, 100); //Assume owners has max 100 shops
			modelMap.put("shopList", se.getShopList());
			// get shop list and store as key as permission. all accounts can only operate their own store
			request.getSession().setAttribute("shopList", se.getShopList()); 
			modelMap.put("user", user); //show user name in front end
			modelMap.put("success", true);
		} catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/getshopbyid", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopById(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId > -1) { //received from front end
			try {
				Shop shop = shopService.getByShopId(shopId);
				List<Area> areaList = areaService.getAreaList();
				modelMap.put("shop", shop);
				modelMap.put("areaList", areaList);
				modelMap.put("success",true);
			} catch(Exception e){
				modelMap.put("success",false);
				modelMap.put("errMsg",e.toString());
			}
		} else {
			modelMap.put("success",false);
			modelMap.put("errMsg","empty shopId");
		}
		return modelMap;
	}
	@RequestMapping(value = "/getshopinitinfo", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopInitInfo(){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		List<Area> areaList = new ArrayList<Area>();
		try {
			shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
			areaList = areaService.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/registershop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "Incorrect Verification Code");
			return modelMap;
		}
		// 1.receive and convert parameters, including shop info and image info
		// Uses fasterXML.Jackson
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {//convert shop info to Shop.class
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if(commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "upload image cannot be empty");
			return modelMap;
		}
		// 2.register shop
		if (shop!= null && shopImg !=null) {
			// initialize owner 
			// Session [Session id stored in cookie and returned to client, Tomcat session expire time is 30min]
			UserInfo owner = (UserInfo) request.getSession().getAttribute("user");
			shop.setOwner(owner);
			ShopExecution se;
			try {
				ImageConstructor imageConstructor = new ImageConstructor(shopImg.getOriginalFilename(),shopImg.getInputStream());
				se = shopService.addShop(shop, imageConstructor);
				if(se.getState() == ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
					//List of shop that can be modified by this user
					@SuppressWarnings("unchecked")
					List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
					if(shopList == null || shopList.size() == 0) {
						shopList = new ArrayList<Shop>();
					} 
					shopList.add(se.getShop());
					request.getSession().setAttribute("shopList", shopList);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "Please enter shop information");
			return modelMap;
		}
		// 3.return result [Included in all try catch blocks]
	}
	
	@RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "Incorrect Verification Code");
			return modelMap;
		}
		// 1.receive and convert parameters, including shop info and image info
		// Uses fasterXML.Jackson
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {//convert shop info to Shop.class
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if(commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}
		// 2. modify shop
		// don't need shop thumbnail image, shop id cannot be null
		if (shop!= null && shop.getShopId() !=null) {
			ShopExecution se;
			try {
				if(shopImg == null) 
					se = shopService.modifyShop(shop, null);
				else {
					ImageConstructor imageConstructor = new ImageConstructor(shopImg.getOriginalFilename(),shopImg.getInputStream());
					se = shopService.modifyShop(shop, imageConstructor);
				}
				if(se.getState() == ShopStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "Please enter shop id");
			return modelMap;
		}
		// 3.return result [Included in all try catch blocks]
	}
	// CommonsMultipartFile has built in toInputStream, use it to change to file
	/*
	 * private static void inputStreamToFile(InputStream ins, File file) {
	 * FileOutputStream os = null; try { os = new FileOutputStream(file); int
	 * bytesRead = 0; byte[] buffer = new byte[1024]; while((bytesRead =
	 * ins.read(buffer))!= -1) { os.write(buffer, 0, bytesRead); } } catch
	 * (Exception e){ throw new
	 * RuntimeException("Error running method inputStreamToFile:" + e.getMessage());
	 * }finally { try { if(os!= null) { os.close(); } if(ins != null) { ins.close();
	 * } } catch(IOException e) { throw new
	 * RuntimeException("Error when inputStreamToFile method tries to close IO :" +
	 * e.getMessage()); } } }
	 */
}
