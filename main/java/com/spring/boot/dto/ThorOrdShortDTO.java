package com.spring.boot.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ThorOrdShortDTO {
	
	private int quantity, car_num, cus_num, total_amount,car_shortp,car_year,car_inwon;
	private String tid, inDate,outDate,staydate,car_type, car_name, car_col, car_place, car_make, sub, car_mileage, car_km, car_fuel, car_fileurl;
	private Date created_at;

}
