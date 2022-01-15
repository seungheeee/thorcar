package com.spring.boot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.spring.boot.service.KakaoPay;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.spring.boot.service.KakaoPay;


@Component
public class Scheduler {
   
   
   @Autowired
   KakaoPay setrequestSubPay;
   
   //스케줄러
    private ThreadPoolTaskScheduler scheduler;
    
    
    public void stopScheduler() {
       //구독 취소 시 scheduler shutdown을 통해 결제 요청 멈춤
        scheduler.shutdown();
    }
 
    public void startScheduler() throws ParseException {
       String today = null;
       Date date = new Date();
       System.out.println("현재시각 : " + date);
       // 포맷변경 ( 년월일 시분초)
       SimpleDateFormat sdformat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
       Calendar cal = Calendar.getInstance();
       
       cal.add(Calendar.SECOND, 5);
        
       today = sdformat.format(cal.getTime());  
       System.out.println("30초후 : " + today); //05/13/2021 13:28:57
       
       Date startTime = sdformat.parse(today);

       scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        // 스케쥴러가 시작되는 부분 
        scheduler.scheduleWithFixedDelay(getRunnable(), startTime, 30000);
    }
    
    private Runnable getRunnable(){
       
        return () -> {
        
           setrequestSubPay.requestSubPay();
        };
    }
 
    
}