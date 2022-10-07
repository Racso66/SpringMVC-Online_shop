package com.project1.o2o.util;

public class PathUtil {
	private static String separator = System.getProperty("file.separator");
	public static String getImgPath() {
		String os = System.getProperty("os.name");
		String path ="";
		if(os.toLowerCase().startsWith("win")) {
			path = "C:/Users/SAO/Desktop/Dinoland pics/";
		}else {
			path = "/home/image/";
		}
		path = path.replace("/", separator);
		return path;
	}
	public static String getShopImagePath(long shopId) {
		String imagePath = "upload/item/shop/" + shopId + "/";
		return imagePath.replace("/", separator);
	}
		
}
