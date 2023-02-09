package com.project1.o2o.util.wechat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/*
 * Wechat request examination util
 *
 */
public class SignUtil {
	private static String token = "o2o";
	
	/**
	 * Examine signature
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static boolean checkSignature(String signature, String timestamp, String nonce) {
		String[] arr = new String[] {token, timestamp, nonce};
		Arrays.sort(arr);
		//append values into string builder in order
		StringBuilder content = new StringBuilder();
		for(int i = 0; i < arr.length; i ++) content.append(arr[i]);
		MessageDigest md = null;
		String tmpStr = null;
		
		try {
			md = MessageDigest.getInstance("SHA-1");
			//Concatenate into one string for sha1 encoding
			byte[] digest = md.digest(content.toString().getBytes());
			tmpStr = byteToStr(digest);
		}catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		content = null;
		//sha1 encoded string is compared with signature. Deciding if request comes from Wechat.
		return tmpStr != null? tmpStr.equals(signature.toUpperCase()):false;
	}
	
	/**
	 * convert byte array to hexadecimal String
	 * @param byteArray
	 * @return
	 */
	private static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		for(int i = 0; i < byteArray.length; i ++) strDigest += byteToHexStr(byteArray[i]);
		return strDigest;
	}
	
	/**
	 * convert byte to hexadecimal String (bottom level)
	 * @param b
	 * @return
	 */
	private static String byteToHexStr(byte b) {
		char[] Digit = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(b>>>4) & 0X0F];
		tempArr[1] = Digit[b & 0X0F];
		String s = new String(tempArr);
		return s;
	}
	
	
}
