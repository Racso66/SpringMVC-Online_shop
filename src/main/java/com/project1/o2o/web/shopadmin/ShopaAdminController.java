package com.project1.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "shopadmin", method = {RequestMethod.GET})
public class ShopaAdminController {
	@RequestMapping(value = "/shopoperation") //view resolver set in spring-web.xml
	public String shopOperation() {
		return "shop/shopoperation";
	}
	
	@RequestMapping(value = "/shoplist")
	public String shopList() {
		return "shop/shoplist";
	}
	
	@RequestMapping(value = "/shopmanagement")
	public String shopManagement() {
		return "shop/shopmanagement";
	}
	
	@RequestMapping(value = "/productcategorymanagement", method = RequestMethod.GET)
	public String productCategoryManagement() {
		return "shop/productcategorymanagement";
	}
	
	@RequestMapping(value = "/productoperation")
	public String productOperation() {
		return "shop/productoperation";//return to add and modify product page
	}
	
	@RequestMapping(value = "/productmanagement")
	public String productManagement() {
		return "shop/productmanagement";
	}
}
