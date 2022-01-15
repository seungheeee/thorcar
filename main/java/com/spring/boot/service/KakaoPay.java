package com.spring.boot.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.spring.boot.dto.AmountVO;
import com.spring.boot.dto.CancelVO;
import com.spring.boot.dto.KakaoPayApprovalVO;
import com.spring.boot.dto.KakaoPayReadyVO;
import com.spring.boot.mapper.ThorService;

import lombok.extern.java.Log;

@Service
@Log
public class KakaoPay {
   
   private static final String HOST = "https://kapi.kakao.com";
    
    private KakaoPayReadyVO kakaoPayReadyVO;
    
    KakaoPayApprovalVO kakaoPayApprovalVO = new KakaoPayApprovalVO();
    CancelVO cancelVO = new CancelVO();
    AmountVO amountvo = new AmountVO();
    ThorService thorservice;
    
    public String kakaoPayReady(String sublevel, String subprice) {
 
        RestTemplate restTemplate = new RestTemplate();
        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + "2e285004c0c322c43a4566bd188b77af");
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
        
        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TCSUBSCRIP");
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", "thorcar");
        params.add("item_name", sublevel);
        params.add("quantity", "1");
        params.add("total_amount", subprice);
        params.add("tax_free_amount", "0");
        params.add("approval_url", "http://localhost:8080/kakaoPaySuccess");
        params.add("cancel_url", "http://localhost:8080/kakaoPayCancel");
        params.add("fail_url", "http://localhost:8080/kakaoPaySuccessFail");
        System.out.println("total_amount1");
        System.out.println(subprice);

        
        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);
 
        try {
            kakaoPayReadyVO = restTemplate.postForObject(new URI(HOST + "/v1/payment/ready"), body, KakaoPayReadyVO.class);
            
            log.info("" + kakaoPayReadyVO);
            
            System.out.println(kakaoPayReadyVO.getNext_redirect_pc_url());
            kakaoPayReadyVO.setTot(subprice);
            return kakaoPayReadyVO.getNext_redirect_pc_url();
            
        } catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return "/pay";
        
    }
    

    
    public KakaoPayApprovalVO kakaoPayInfo(String pg_token) throws Exception {
        
        log.info("KakaoPayInfoVO............................................");
        log.info("-----------------------------");
        
        RestTemplate restTemplate = new RestTemplate();
        
        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + "2e285004c0c322c43a4566bd188b77af");
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
 
        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TCSUBSCRIP");
        params.add("tid", kakaoPayReadyVO.getTid());
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", "thorcar");
        params.add("pg_token", pg_token);
        params.add("total_amount", kakaoPayReadyVO.getTot());
        System.out.println("total_amount2");
        System.out.println(kakaoPayReadyVO.getTot());
        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        
        try {
            kakaoPayApprovalVO = restTemplate.postForObject(new URI(HOST + "/v1/payment/approve"), body, KakaoPayApprovalVO.class);
            log.info("" + kakaoPayApprovalVO);
                        
            return kakaoPayApprovalVO;
        
        } catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }

    //void로 하지말고 vo를 리턴해줘야할듯
    //섭페이를 실행하고나서도 돌려주는 값들이 있다, 서버에 담으려면 vo필요
 public KakaoPayApprovalVO requestSubPay() {
        
        RestTemplate restTemplate = new RestTemplate();
         
         // 서버로 요청할 Header
         HttpHeaders headers = new HttpHeaders();
         headers.add("Authorization", "KakaoAK " + "2e285004c0c322c43a4566bd188b77af");
         headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
         headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
         
         
         // 서버로 요청할 Body
         MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
         params.add("sid", kakaoPayApprovalVO.getSid());
         params.add("cid", "TCSUBSCRIP");
         params.add("partner_order_id", "1001");
         params.add("partner_user_id", "thorcar");
         params.add("quantity", "1");
         params.add("total_amount", kakaoPayReadyVO.getTot());
         params.add("tax_free_amount", "0");
         
         HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);
  
         try {
            kakaoPayApprovalVO = restTemplate.postForObject(new URI(HOST + "/v1/payment/subscription"), body, KakaoPayApprovalVO.class);
              
             log.info("" + kakaoPayReadyVO);
             System.out.println("2차결제 실행입니다요");
             
             return kakaoPayApprovalVO;
              
         } catch (RestClientException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         } catch (URISyntaxException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
         return null;
         
     }
   
    public CancelVO kakaoPayCancel() {
        
        RestTemplate restTemplate = new RestTemplate();
         
         // 서버로 요청할 Header
         HttpHeaders headers = new HttpHeaders();
         headers.add("Authorization", "KakaoAK " + "2e285004c0c322c43a4566bd188b77af");
         headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
         headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
         
         
         // 서버로 요청할 Body
         MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
         System.out.println(kakaoPayApprovalVO.getSid());
         params.add("sid", kakaoPayApprovalVO.getSid());
         params.add("cid", "TCSUBSCRIP");
         
         HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);
  
         try {
             cancelVO = restTemplate.postForObject(new URI(HOST + "/v1/payment/manage/subscription/inactive"), body, CancelVO.class);
             System.out.println("구독취소입니다요");
             
             return cancelVO;
            
         } catch (RestClientException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         } catch (URISyntaxException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
         
         return null;
         
     }

    public String onePayReady(int shortPrice, String carName) {
    	 
        RestTemplate restTemplate = new RestTemplate();
        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + "2e285004c0c322c43a4566bd188b77af");
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
        
        
        
        // 서버로 요청할 Body
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", "thorcar");
        params.add("item_name", carName);
        params.add("quantity", "1");
        params.add("total_amount", shortPrice);
        params.add("tax_free_amount", "0");
        params.add("approval_url", "http://localhost:8080/onePaySuccess");
        params.add("cancel_url", "http://localhost:8080/onePayCancel");
        params.add("fail_url", "http://localhost:8080/onePaySuccessFail");
        
        
        
        HttpEntity<MultiValueMap<String, Object>> body = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
 
        try {
            kakaoPayReadyVO = restTemplate.postForObject(new URI(HOST + "/v1/payment/ready"), body, KakaoPayReadyVO.class);
            
            log.info("" + kakaoPayReadyVO);
            kakaoPayReadyVO.setShorttot(shortPrice); 
            System.out.println(shortPrice);
            
            return kakaoPayReadyVO.getNext_redirect_pc_url();
            
        } catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return "/pay";
        
    }
   
    
    
public KakaoPayApprovalVO onePayInfo(String pg_token) throws Exception {
        
        log.info("onePayInfoVO............................................");
        log.info("-----------------------------");
        
        RestTemplate restTemplate = new RestTemplate();
        
        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + "2e285004c0c322c43a4566bd188b77af");
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
 
        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", kakaoPayReadyVO.getTid());
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", "thorcar");
        params.add("pg_token", pg_token);
        
        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        
        try {
        	
            kakaoPayApprovalVO = restTemplate.postForObject(new URI(HOST + "/v1/payment/approve"), body, KakaoPayApprovalVO.class);
            log.info("" + kakaoPayApprovalVO);
                        
            return kakaoPayApprovalVO;
        
        } catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
}