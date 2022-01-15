package com.spring.boot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;


import com.spring.boot.dto.LentDTO;

@Mapper
public interface LentMapper {

	public List<LentDTO> getLists() throws Exception;
	
	public LentDTO getReadData(String place) throws Exception;
	
}
