package com.spring.boot.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ThorOrdDTO {
	private int quantity, car_num, cus_num, total_amount;
	private String sid, lastdate;
	private Date created_at;
	 
}
