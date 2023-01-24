package com.project1.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.project1.o2o.entity.HeadLine;

public interface HeadLineDao {
	/**
	 * Return list of head line based on head line search conditions. (name of head line)
	 * @param headLineCondition
	 * @return
	 */
	List<HeadLine> queryHeadLine(@Param("headLineCondition") HeadLine headLineCondition);
}
