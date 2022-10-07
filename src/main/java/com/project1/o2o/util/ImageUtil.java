package com.project1.o2o.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
	private static String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final Random r = new Random();
	private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);
	
	/**
	 * convert CommonsMultipartFile to file.
	 * CommonsMultipartFile is hard to initialize. It uses FileItem which is difficult for java.io.File to convert to
	 * CommonsMultipartFile has a built in function transferTo File. It is easier for service level unit testing.
	 * @param cFile
	 * @return
	 */
	public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {
		File newFile = new File(cFile.getOriginalFilename());
		try {
			cFile.transferTo(newFile);
		} catch (IllegalStateException e) {
			logger.error(e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return newFile;
	}
	/**
	 * compress image and return new image's path
	 * @param thumbnail
	 * @param targetAddr
	 * @return
	 */
	public static String generateThumbnail(InputStream thumbnailInputStream, String fileName, String targetAddr) {
		String realFileName = getRandomFileName();
		String extension = getFileExtension(fileName);
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;
		logger.debug("current relativeAddr is:" + relativeAddr);
		File dest = new File(PathUtil.getImgPath() + relativeAddr);
		logger.debug("current complete address is:" + PathUtil.getImgPath() + relativeAddr);
		try {
			Thumbnails.of(thumbnailInputStream).size(200, 200)
			.watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(path + "/watermark.jpg")),0.25f)
			.outputQuality(0.8f)
			.toFile(dest);
		} catch(IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return relativeAddr; // use relative address for usability on different operating systems.
	}
	/**
	 * create path for target directory. if path is /D:/path1/path2/path3/xxx.jpg, create path1 2 and 3.
	 * @param targetAddr
	 */
	private static void makeDirPath(String targetAddr) {
		String realFileParentPath = PathUtil.getImgPath() + targetAddr;
		File dirPath = new File(realFileParentPath);
		if(!dirPath.exists()) {
			dirPath.mkdirs();
		}
	}
	/**
	 * get extension of image. jpg png...
	 * @param thumbnail
	 * @return
	 */
	private static String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}
	/**
	 * Random file name are unique, use current time + 5 RNG numbers
	 * @return
	 */
	public static String getRandomFileName() {
		// Generate 5 digit RNG
		int rannum = r.nextInt(89999)+10000; // 99999> rannum >10000 
		String currTimeStr = sDateFormat.format(new Date());
		return currTimeStr + rannum;
	}

	public static void main(String[] args) throws IOException {
		Thumbnails.of(new File("C:\\Users\\SAO\\Desktop\\Dinoland pics\\2.jpg"))
		.size(200, 200)
		.watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(path + "/watermark.jpg")),0.25f)
		.outputQuality(0.8f)
		.toFile("C:\\Users\\SAO\\Desktop\\Dinoland pics\\2new.jpg");
	}
}
