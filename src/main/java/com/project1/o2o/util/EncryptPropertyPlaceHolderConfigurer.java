package com.project1.o2o.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptPropertyPlaceHolderConfigurer extends PropertyPlaceholderConfigurer{
	// properties to be decrypted
	private String[] encryptedPropNames = {"jdbc.username", "jdbc.password"};
	
	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		if(isEncryptProp(propertyName)) {
			String decryptValue = DESUtil.getDecryptedString(propertyValue);
			return decryptValue;
		} else {
			return propertyValue;
		}
	}

	private boolean isEncryptProp(String propertyName) {
		for(String encryptpropertyName: encryptedPropNames) {
			if(encryptpropertyName.equals(propertyName)) return true;
		}
		return false;
	}
}
