package com.project1.o2o.web.wechat;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.project1.o2o.util.wechat.SignUtil;

@Controller
@RequestMapping("wechat")
public class WechatController {
	private static Logger log = LoggerFactory.getLogger(WechatController.class);
	
	@RequestMapping(method = {RequestMethod.GET})//don't need to specify routing for servlet
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		log.debug("wechat get...");
		//Wechat encoded signature which includes token timestamp and nonce parameters
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		// random number
		String nonce = request.getParameter("nonce");
		// random string
		String echostr = request.getParameter("echostr");
		//check with signature for requests. If examination succeeds, return echostr, else means fail
		PrintWriter out = null;
		try {
			out = response.getWriter();
			if(SignUtil.checkSignature(signature, timestamp, nonce)) {
				log.debug("wechat get succesful...");
				out.print(echostr);
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(out!=null) out.close();
		}
	}
}
