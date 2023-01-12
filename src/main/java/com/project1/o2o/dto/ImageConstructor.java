package com.project1.o2o.dto;

import java.io.InputStream;

public class ImageConstructor {
	private String imageName;
	private InputStream image;
	
	public ImageConstructor(String imageName, InputStream image) {
		this.imageName = imageName;
		this.image = image;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public InputStream getImage() {
		return image;
	}

	public void setImage(InputStream image) {
		this.image = image;
	}
	
	
}
