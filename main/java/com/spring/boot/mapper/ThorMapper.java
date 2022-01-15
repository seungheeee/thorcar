package com.spring.boot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.spring.boot.dto.ThorCarDTO;
import com.spring.boot.dto.ThorClientDTO;
import com.spring.boot.dto.ThorOrdDTO;
import com.spring.boot.dto.ThorOrdShortDTO;

@Mapper
public interface ThorMapper {
	
	public void insertData1(ThorOrdDTO dto) throws Exception;
	
	public int maxNum1() throws Exception;
	
	public ThorCarDTO getReadData1(int num) throws Exception;
	
	public int maxNum() throws Exception;
	
	public void insertData(ThorClientDTO dto) throws Exception; 

	public ThorClientDTO getReadData(String id) throws Exception;

	public ThorClientDTO getReadTel(String tel) throws Exception;

	public void updatePwd(ThorClientDTO dto) throws Exception;

	public int idCheck(ThorClientDTO dto) throws Exception;
	
	public int telCheck(ThorClientDTO dto) throws Exception;
	
	public List<ThorCarDTO> getLists() throws Exception;

	public List<ThorCarDTO> getListsFromSearchValue(String searchValue) throws Exception;

	public int getReadnumfromcurrentid(String id) throws Exception;
	
	public List<ThorOrdDTO> getreaddatafromcusnum(int cus_num) throws Exception;

	public void deleteData(int num) throws Exception;
	
	public void insertDataordshort(ThorOrdShortDTO dto) throws Exception;
	
	public List<ThorOrdShortDTO> getListshort() throws Exception;

}