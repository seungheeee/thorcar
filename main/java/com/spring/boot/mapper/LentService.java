package com.spring.boot.mapper;

import java.util.List;

import com.spring.boot.dto.LentDTO;

public interface LentService {

	public List<LentDTO> getLists() throws Exception;
	
	public LentDTO getReadData(String place) throws Exception;
	
}
