package com.project1.o2o.util;

/*
 * PageCalculator used for transition of rowIndex to pageIndex calculations
 */
public class PageCalculator {
	public static int calculateRowIndex(int pageIndex, int pageSize) {
		return (pageIndex > 0) ? (pageIndex -1) * pageSize : 0; //select from page index -1 and amount based on pageSize
	}
}
