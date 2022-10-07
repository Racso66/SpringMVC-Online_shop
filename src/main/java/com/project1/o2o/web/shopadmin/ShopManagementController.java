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
		try {
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
			// initialize owner TODO Session
			UserInfo owner = new UserInfo();
			owner.setUserId(1L);
			shop.setOwner(owner);
			ShopExecution se;
			try {
				se = shopService.addShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
				if(se.getState() == ShopStateEnum.CHECK.getState()) {
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
			modelMap.put("errMsg", "Please enter shop information");
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
