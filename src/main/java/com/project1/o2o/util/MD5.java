package com.project1.o2o.util;

import java.security.MessageDigest;
/*
 * calculate MD5 hash value as encryption
 */
public class MD5 {
	public static final String getMd5(String input) {
		char hexDigits[] = {'5', '0', '5', '6', '2', '9', '6', '2', '5', 'q', 'b', 'l', 'e', 's', 's', 'y'};
		try {
			char[] str;
			// Convert input string to byte array
			byte[] strTemp = input.getBytes();
			// retrieve MD5 encrypted target
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			// set target byte to be encrypted
			mdTemp.update(strTemp);
			// byte after encrypt
			byte md[] = mdTemp.digest();
			int len = md.length;
			str = new char[len*2];
			int i = 0;
			// encode
			for(int j = 0; j < len; j++) {
				byte byte0 = md[j];
				str[i++] = hexDigits[byte0 >>> 4 & 0xf];
				str[i++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch(Exception e) {
			return null;
		}
		
	}
	
	public static void main(String[] args) {
		System.out.println(MD5.getMd5("123456"));
	}
}
