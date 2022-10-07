package com.project1.o2o.web.superadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project1.o2o.entity.Area;
import com.project1.o2o.service.AreaService;

@Controller
@RequestMapping("/superadmin")
public class AreaController {
	Logger logger = LoggerFactory.getLogger(AreaController.class);
	
	
	//*************TESTING***************//
	/*
	 * @RequestMapping(value = "/listar", method = RequestMethod.GET)
	 * 
	 * @ResponseBody private String Hello() {
	 * 
	 * return "Hello"; }
	 */
	
	@Autowired
	private AreaService areaService;

	@RequestMapping(value = "/listarea", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listArea() { // key = String, value = Object
		logger.info("###START###");
		long startTime = System.currentTimeMillis();
		Map<String, Object> modelMap = new HashMap<String, Object>(); // key is unique in hash map. modelMap used to store return value
		List<Area> list = new ArrayList<Area>(); // used to return service level Area list.
		try {
			list = areaService.getAreaList();
			// framework used for superadmin is easyUI, uses names "rows" and "total".
			modelMap.put("rows", list);
			modelMap.put("total", list.size());
		} catch (Exception e) {
			e.printStackTrace();
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		logger.error("Test error...");
		long endTime = System.currentTimeMillis();
		logger.debug("costTime:[{}ms]", endTime-startTime);
		logger.info("###END###");
		return modelMap;
	}
}
