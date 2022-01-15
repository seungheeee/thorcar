package com.spring.boot.controller;

import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spring.boot.Scheduler;
import com.spring.boot.dto.AmountVO;
import com.spring.boot.dto.KakaoPayApprovalVO;
import com.spring.boot.dto.KakaoPayReadyVO;
import com.spring.boot.dto.LoginDTO;
import com.spring.boot.dto.MainDateDTO;
import com.spring.boot.dto.ThorCarDTO;
import com.spring.boot.dto.ThorOrdDTO;
import com.spring.boot.dto.ThorOrdShortDTO;
import com.spring.boot.mapper.ThorService;
import com.spring.boot.service.KakaoPay;

import lombok.Setter;
import lombok.extern.java.Log;

@Log
@Controller
public class Kakao {
   
   @Resource
   private ThorService thorService;
   
   @Resource
   Scheduler scheduler;

   
   @Setter(onMethod_ = @Autowired)
    private KakaoPay kakaopay;
    
    
    @GetMapping("kakaoPay")
    public void kakaoPayGet() {
        
    }
    
    @PostMapping("onePay")
    public String onepay(HttpServletRequest request, KakaoPayReadyVO vo,ThorCarDTO c_dto) {
    	
    	log.info("onepay 시작............................................");
    	HttpSession session = request.getSession();
    	c_dto = (ThorCarDTO) session.getAttribute("ThorCarDTO");
    	
    	int shortPrice = c_dto.getCar_shortp();
    	String carName = c_dto.getCar_name();
    	return "redirect:" + kakaopay.onePayReady(shortPrice,carName);
    	
	   }
    
    @GetMapping("onePaySuccess")
    public ModelAndView onePaySuccess(@RequestParam("pg_token") String pg_token, ThorCarDTO c_dto,
          Model model, LoginDTO l_dto, ThorOrdShortDTO dto, MainDateDTO m_dto, HttpServletRequest request) throws Exception {
        log.info("onePaySuccess get............................................");
        log.info("onePaySuccess pg_token : " + pg_token);
        HttpSession session = request.getSession();
        
        ModelAndView mav = new ModelAndView();
        KakaoPayApprovalVO vo = new KakaoPayApprovalVO();
        
        l_dto = (LoginDTO)session.getAttribute("loginDTO");
        c_dto = (ThorCarDTO) session.getAttribute("ThorCarDTO");
        m_dto = (MainDateDTO)session.getAttribute("MainDateDTO");
        
        //System.out.println("내가 필요한거" + cus_num);
        //System.out.println("내가 필요한거" + car_num);
        
        vo = kakaopay.onePayInfo(pg_token);
        model.addAttribute("info", vo);
        AmountVO a_vo = new AmountVO();
       
        a_vo = vo.getAmount();
        	
        dto.setTid(vo.getTid());
        dto.setQuantity(vo.getQuantity());
        dto.setCreated_at(vo.getCreated_at());
        dto.setInDate(m_dto.getInDate());
        dto.setOutDate(m_dto.getOutDate());
        dto.setStaydate(m_dto.getStaydate());
        dto.setTotal_amount(a_vo.getTotal());
        dto.setCar_num(c_dto.getNum());
        dto.setCus_num(l_dto.getNum());
        dto.setCar_type(c_dto.getCar_type());
        dto.setCar_name(c_dto.getCar_name());
        dto.setCar_col(c_dto.getCar_col());
        dto.setCar_shortp(c_dto.getCar_shortp());
        dto.setCar_place(c_dto.getCar_place());
        dto.setCar_make(c_dto.getCar_make());
        dto.setSub(c_dto.getSub());
        dto.setCar_year(c_dto.getCar_year());
        dto.setCar_mileage(c_dto.getCar_mileage());
        dto.setCar_inwon(c_dto.getCar_inwon());
        dto.setCar_km(c_dto.getCar_km());
        dto.setCar_fuel(c_dto.getCar_fuel());
        dto.setCar_fileurl(c_dto.getCar_fileurl());
        
        thorService.insertDataordshort(dto);
        
        mav.setViewName("thorcar/onePaySuccess");
        return mav;
    }
	
    @PostMapping("kakaoPay")
    public String kakaoPay(KakaoPayReadyVO vo,HttpServletRequest request) {
        log.info("kakaoPay post............................................");
        
        String sublevel = request.getParameter("sublevel");
        String subprice;
        
        if(sublevel==null || (sublevel.equals(""))) {
           
           sublevel = "Basic";
           subprice = "590000";
           
        }else {
           subprice = request.getParameter("subprice");
        }
        
        return "redirect:" + kakaopay.kakaoPayReady(sublevel, subprice);
    }
    
    
    @GetMapping("kakaoPaySuccess")
    public ModelAndView kakaoPaySuccess(@RequestParam("pg_token") String pg_token, 
          Model model, ThorOrdDTO dto, HttpSession session) throws Exception {
        log.info("kakaoPaySuccess get............................................");
        log.info("kakaoPaySuccess pg_token : " + pg_token);
        ModelAndView mav = new ModelAndView();
        KakaoPayApprovalVO vo = new KakaoPayApprovalVO();
        
        LoginDTO l_dto = (LoginDTO)session.getAttribute("loginDTO");
        
        int currentnum = thorService.getReadnumfromcurrentid(l_dto.getId());
        
        vo = kakaopay.kakaoPayInfo(pg_token);
        AmountVO a_vo = new AmountVO();
        model.addAttribute("info", vo);
        
        a_vo = vo.getAmount();
        	
        dto.setSid(vo.getSid());
        dto.setCar_num(0);
        dto.setCus_num(currentnum);
        dto.setTotal_amount(a_vo.getTotal());
        dto.setLastdate("1993/06/18");
        dto.setCreated_at(vo.getCreated_at());
        dto.setQuantity(1);
        model.addAttribute("dto", dto);
        
        thorService.insertData1(dto);
        scheduler.startScheduler();
        mav.setViewName("thorcar/kakaoPaySuccess");
      return mav;
    }

    @GetMapping("kakaoPayCancel")
    public void kakaoPayCancelGet() {
       scheduler.stopScheduler();
        
    }
    
    @PostMapping("kakaoPayCancel")
    public void kakaoPayCancel(HttpServletResponse response, HttpSession session) throws Exception {
        log.info("kakaoPay post............................................");
        kakaopay.kakaoPayCancel();
        
        LoginDTO l_dto = (LoginDTO)session.getAttribute("loginDTO");
        int currentnum = thorService.getReadnumfromcurrentid(l_dto.getId());
        
        thorService.deleteData(currentnum);
        response.setContentType("text/html; charset=UTF-8");
        
        PrintWriter out = response.getWriter();
         
        out.println("<script>alert('구독이 취소되었습니다.'); location.href='/myPage.action';</script>");
        
        scheduler.stopScheduler();
         
        out.flush();
    }
}