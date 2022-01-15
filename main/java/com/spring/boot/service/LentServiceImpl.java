package com.spring.boot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.boot.dto.LentDTO;
import com.spring.boot.mapper.LentMapper;
import com.spring.boot.mapper.LentService;

@Service
public class LentServiceImpl implements LentService{
	
	@Autowired
	private LentMapper lentMapper;

	@Override
	public List<LentDTO> getLists() throws Exception {
		return lentMapper.getLists();
	}

	@Override
	public LentDTO getReadData(String place) throws Exception {
		return lentMapper.getReadData(place);
	}
	
	

}
