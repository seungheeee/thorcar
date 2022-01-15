package com.spring.boot.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CancelVO {
	
	private String cid, sid, status;
	private Date created_at, last_approved_at, inactivated_at;

}
