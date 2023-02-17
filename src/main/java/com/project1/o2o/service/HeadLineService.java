package com.project1.o2o.service;

import java.io.IOException;
import java.util.List;

import com.project1.o2o.entity.HeadLine;

public interface HeadLineService {
	public static final String HLLISTKEY = "headlinelist";
	
	/**
	 * Return head line list according to search conditions
	 * 
	 * @param headLineCondition
	 * @return
	 * @throws IOException
	 */
	List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException;
}
