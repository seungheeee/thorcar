package com.spring.boot.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.boot.dto.ThorCarDTO;
import com.spring.boot.dto.ThorClientDTO;
import com.spring.boot.dto.ThorOrdDTO;
import com.spring.boot.dto.ThorOrdShortDTO;
import com.spring.boot.mapper.ThorMapper;
import com.spring.boot.mapper.ThorService;

@Service
public class ThorServiceImpl implements ThorService  {
	
	@Autowired
	private ThorMapper thorMapper;

	@Override
	public int maxNum() throws Exception {
		return thorMapper.maxNum();
	}

	@Override
	public void insertData(ThorClientDTO dto) throws Exception {
		thorMapper.insertData(dto);
	}

	@Override
	public ThorClientDTO getReadData(String id) throws Exception {
		
		return thorMapper.getReadData(id);
	}

	@Override
	public ThorClientDTO getReadTel(String tel) throws Exception {
		return thorMapper.getReadTel(tel);
	}

	@Override
	public void updatePwd(ThorClientDTO dto) throws Exception {
		thorMapper.updatePwd(dto);
	}

	
	@Override
	public int idCheck(ThorClientDTO dto) throws Exception {
		int result = thorMapper.idCheck(dto);
		return result;
	}

	@Override
	public int telCheck(ThorClientDTO dto) throws Exception {
		int result = thorMapper.telCheck(dto);
		return result;
	}

	@Override
	public int maxNum1() throws Exception {
		return thorMapper.maxNum1();
	}


	@Override
	public ThorCarDTO getReadData1(int num) throws Exception {
		return thorMapper.getReadData1(num);
	}

	@Override
	public void insertData1(ThorOrdDTO dto) throws Exception {
		thorMapper.insertData1(dto);
		
	}

	@Override
	public List<ThorCarDTO> getLists() throws Exception {
		return thorMapper.getLists();
	}

	@Override
	public List<ThorCarDTO> getListsFromSearchValue(String searchValue) throws Exception {
		// TODO Auto-generated method stub
		return thorMapper.getListsFromSearchValue(searchValue);
	}

	@Override
	public int getReadnumfromcurrentid(String id) throws Exception {
		
		return thorMapper.getReadnumfromcurrentid(id);
	}

	@Override
	public List<ThorOrdDTO> getreaddatafromcusnum(int cus_num) throws Exception {
      return thorMapper.getreaddatafromcusnum(cus_num);
	}

	@Override
	public void deleteData(int num) throws Exception {
		thorMapper.deleteData(num);
	}

	@Override
	public void insertDataordshort(ThorOrdShortDTO dto) throws Exception {
		thorMapper.insertDataordshort(dto);
	}

	@Override
	public List<ThorOrdShortDTO> getListshort() throws Exception {
		return thorMapper.getListshort();
	}
	

	
}