package com.project1.o2o.util;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

// DES symmetric encryption uses same key for encoding and decoding
public class DESUtil {
	private static Key key;
	private static String KEY_STR = "myKey";
	private static String CHARSETNAME = "UTF-8";
	private static String ALGORITHM = "DES";
	
	// generate key
	static {
		try {
			// generate DES algorithm target
			KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
			// SHA1 security
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			// set seed for key
			secureRandom.setSeed(KEY_STR.getBytes());
			// initialize the algorithm target based on SHA1
			generator.init(secureRandom);
			key = generator.generateKey();
			generator = null;
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	// Encrypt
	public static String getEncryptedString(String str) {
		// Receive Byte[] and convert to String based on BASE64 encoder
		BASE64Encoder base64encoder = new BASE64Encoder();
		try {
			//charset-utf8
			byte[] bytes = str.getBytes(CHARSETNAME);
			// get encrypt target
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			// Initialize encode info
			cipher.init(Cipher.ENCRYPT_MODE, key);
			// encode
			byte[] doFinal = cipher.doFinal(bytes);
			// convert byte[] to coded String
			return base64encoder.encode(doFinal);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	// Decrypt
	public static String getDecryptedString(String str) {
		BASE64Decoder base64decoder = new BASE64Decoder();
		try {
			// Decode String to byte[]
			byte[] bytes = base64decoder.decodeBuffer(str);
			// get decrypt target
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			// initialize decrypt info
			cipher.init(Cipher.DECRYPT_MODE, key);
			// Decrypt bytes
			byte[] doFinal = cipher.doFinal(bytes);
			return new String(doFinal, CHARSETNAME);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(getEncryptedString("root"));
		//System.out.println(getEncryptedString("6201502145"));
	}
}
