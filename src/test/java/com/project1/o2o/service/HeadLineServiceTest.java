package com.project1.o2o.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.project1.o2o.BaseTest;
import com.project1.o2o.entity.HeadLine;

public class HeadLineServiceTest extends BaseTest {
	@Autowired
	private HeadLineService headLineService;

	@Test
	public void testGetHeadLineList() throws IOException, FileNotFoundException {
		List<HeadLine> headLineList = headLineService.getHeadLineList(new HeadLine());
		System.out.println(headLineList.get(0).getLineName()); //give first imgName
		HeadLine headLineCondition = new HeadLine();
		headLineCondition.setEnableStatus(1);
		headLineList = headLineService.getHeadLineList(headLineCondition);
		System.out.println(headLineList.get(0).getLineName()); //give highest priority
	}
}