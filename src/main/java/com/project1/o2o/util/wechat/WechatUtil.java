package com.project1.o2o.util.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project1.o2o.dto.UserAccessToken;
import com.project1.o2o.dto.WechatUser;
import com.project1.o2o.entity.UserInfo;

public class WechatUtil {
	private static Logger log = LoggerFactory.getLogger(WechatUtil.class);
	/*
	 * Retrieve UserAccessToken class
	 */
	public static UserAccessToken getUserAccessToken(String code) throws IOException {
		String appId = "wx4be87ac46480affe";
		log.debug("appId: " + appId);
		String appsecret = "f7eb57e33bc7cd64ba734ff48f9e8faa";
		log.debug("app secret: " + appsecret);
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId
				+ "&secret=" + appsecret + "&code=" + code + "&grant_type=authorization_code";
		//Retrieve token's json string by requesting from the URL
		String tokenStr = httpsRequest(url, "GET", null);
		log.debug("userAccessToken: " + tokenStr);
		UserAccessToken token = new UserAccessToken();
		//Jackson
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			//convert json string to target
			token = objectMapper.readValue(tokenStr, UserAccessToken.class);
		} catch(JsonParseException e) {
			log.error("Failed to retrieve user access token: " + e.getMessage());
			e.printStackTrace();
		}catch(JsonMappingException e) {
			log.error("Failed to retrieve user access token: " + e.getMessage());
			e.printStackTrace();
		} catch(IOException e) {
			log.error("Failed to retrieve user access token: " + e.getMessage());
			e.printStackTrace();
		} 
		if(token == null) {
			log.error("Failed to retrieve user access token,");
			return null;
		}
		return token;	
	}
	
	private static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		StringBuffer buffer = new StringBuffer();
		try {
			//create SSLContext target and use appointed trust manager(Security for Http)
			TrustManager[]tm = {new MyX509TrustManager()};
			SSLContext sslContext = SSLContext.getInstance("SSL","SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			//Retrieve SSLSocketFactory target from the above SSLContext target.
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			
			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			//Request method(GET/POST)
			httpUrlConn.setRequestMethod(requestMethod);
			
			if("GET".equalsIgnoreCase(requestMethod)) httpUrlConn.connect();
			//if there are data to be posted
			if(outputStr != null) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			//convert inputstream received to string
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			
			String str = null;
			while ((str = bufferedReader.readLine()) != null) buffer.append(str);
			//free resources used
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			log.debug("https buffer: " + buffer.toString());
		} catch(ConnectException ce) {
			log.error("Wechat server connection timed out.");
		} catch(Exception e) {
			log.error("https request error:{}", e);
		}
		return buffer.toString();
	}

	public static WechatUser getUserInfo(String accessToken, String openId) {
		//concatenate access token and openId to get userinfo url and port specified by wechat
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" 
				+ openId + "&lang=en_CA";
		//visit the url and retrieve user info json string
		String userStr = httpsRequest(url, "GET", null);
		log.debug("user info :" + userStr);
		WechatUser user = new WechatUser();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			// convert Json string to target
			user = objectMapper.readValue(userStr, WechatUser.class);
		} catch (JsonParseException e) {
			log.error("Failed to retrieve user information: " + e.getMessage());
			e.printStackTrace();
		} catch (JsonMappingException e) {
			log.error("Failed to retrieve user information: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.error("Failed to retrieve user information: " + e.getMessage());
			e.printStackTrace();
		}
		if (user == null) {
			log.error("Failed to retrieve user information.");
			return null;
		}
		return user;
	}
	/**
	 * Convert information from WechatUser to UserInfo and return as UserInfo class
	 *
	 * @param user
	 * @return
	 */
	public static UserInfo getUserInfoFromRequest(WechatUser user) {
		UserInfo userInfo = new UserInfo();
		userInfo.setName(user.getNickName());
		userInfo.setGender(user.getSex() + "");
		userInfo.setThumbnail(user.getHeadimgurl());
		userInfo.setEnableStatus(1);
		return userInfo;
	}
}
