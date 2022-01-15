package com.spring.boot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.boot.dto.BoardDTO;
import com.spring.boot.mapper.BoardMapper;
import com.spring.boot.mapper.BoardService;

@Service
public class BoardServiceImpl implements BoardService{

	@Autowired
	private BoardMapper boardMapper;
	
	@Override
	public int maxNum() throws Exception {
		
		return boardMapper.maxNum();
	}

	@Override
	public void insertData(BoardDTO dto) throws Exception {
		boardMapper.insertData(dto);
		
	}

	@Override
	public int getDataCount(String searchKey, String searchValue) throws Exception {
		
		return boardMapper.getDataCount(searchKey, searchValue);
	}

	@Override
	public List<BoardDTO> getLists(int start, int end, String searchKey, String searchValue) throws Exception {
		
		return boardMapper.getLists(start, end, searchKey, searchValue);
	}

	@Override
	public BoardDTO getReadData(int num) throws Exception {
		
		return boardMapper.getReadData(num);
	}

	@Override
	public void updateHitCount(int num) throws Exception {
		
		boardMapper.updateHitCount(num);
		
	}

	@Override
	public void updateData(BoardDTO dto) throws Exception {

		boardMapper.updateData(dto);
		
	}

	@Override
	public void deleteData(int num) throws Exception {

		boardMapper.deleteData(num);
		
	}

	@Override
	public List<BoardDTO> preReadData(String searchKey, String searchValue,int groupNum,int orderNo) throws Exception{
	
		return boardMapper.preReadData(searchKey, searchValue,groupNum,orderNo);
	}

	@Override
	public List<BoardDTO> nextReadData(String searchKey, String searchValue) throws Exception{
		return boardMapper.nextReadData(searchKey, searchValue);
	}

	@Override
	public void orderNoUpdate(int num) throws Exception {
		boardMapper.orderNoUpdate(num);
		
	}

}
