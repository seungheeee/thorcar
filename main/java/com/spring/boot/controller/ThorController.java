package com.spring.boot.controller;

import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.spring.boot.dto.LentDTO;
import com.spring.boot.dto.LoginDTO;
import com.spring.boot.dto.MainDateDTO;
import com.spring.boot.dto.ThorCarDTO;
import com.spring.boot.dto.ThorClientDTO;
import com.spring.boot.dto.ThorOrdDTO;
import com.spring.boot.dto.ThorOrdShortDTO;
import com.spring.boot.mapper.LentService;
import com.spring.boot.mapper.ThorService;
import com.spring.boot.util.MyUtil;

@RestController
public class ThorController {
	
	@Resource
	private ThorService thorService; //인터페이스 보드서비스 불러옴
	@Resource
	private LentService lentService;
	
	@Autowired
	MyUtil myUtil;
	
	
	//부트에선 string 반환값으로 못씀 무조건 ModelAndView
	@RequestMapping(value = "/main.action",method = RequestMethod.GET)
	   public ModelAndView main(LentDTO dto,HttpServletRequest request) throws Exception{
	      
	      request.setCharacterEncoding("UTF-8");
	      
	      ModelAndView mav = new ModelAndView();
	      
	      mav.setViewName("thorcar/main");
	      return mav;
	      
	   }
	
	
	   @RequestMapping(value = "/reservation.action",method = RequestMethod.GET)
	   public ModelAndView reservation(HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception{
	      
	      ModelAndView mav = new ModelAndView();
	      
	      LoginDTO l_dto = (LoginDTO)session.getAttribute("loginDTO");
	        
	        if(l_dto==null) {
				
				response.setContentType("text/html; charset=UTF-8");
	       		 
	     		PrintWriter out = response.getWriter();
	     		 
	     		out.println("<script>alert('로그인 후 이용가능한 서비스입니다.');</script>");
	     		 
	     		out.flush();

	            mav.setViewName("thorcar/main");
	    		return mav;
				
			}else {
				
				int currentnum = thorService.getReadnumfromcurrentid(l_dto.getId());
		        
		        List<ThorOrdDTO> lists = thorService.getreaddatafromcusnum(currentnum);
		        
		        mav.addObject("lists", lists);
				mav.setViewName("thorcar/reservation");
			      return mav;
			}
	        
	      
	      
	   }
	
	   @RequestMapping(value = "/shortReservation.action",method = RequestMethod.GET)
	   public ModelAndView shortReservation(HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception{
	      
		ModelAndView mav = new ModelAndView();
		  
		List<ThorOrdShortDTO> lists = thorService.getListshort();
		     
		mav.addObject("lists", lists);
		  
		mav.setViewName("thorcar/shortReservation");
		return mav;
	   }
	   
	@RequestMapping(value = "/subscription.action",method = RequestMethod.GET)
	public ModelAndView subscription(HttpServletRequest request) throws Exception{
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("thorcar/subscription");
		return mav;
		
	}
	
	@RequestMapping(value = "/database.action",method = RequestMethod.GET)
	public ModelAndView database(HttpServletRequest request) throws Exception{
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("thorcar/database");
		return mav;
		
	}
	
	@RequestMapping(value = "/shortRent.action",method = RequestMethod.GET)
	public ModelAndView shortRent(HttpServletRequest request) throws Exception{
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("thorcar/shortRent");
		return mav;
		
	}
	
	@RequestMapping(value = "/subInfo.action", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView subInfo(HttpServletRequest request) throws Exception{
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("thorcar/subInfo");

		return mav;
		
	}
	
	@RequestMapping(value = "/login.action", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView login(HttpServletRequest request) throws Exception{
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("thorcar/login");

		return mav;
		
	}
	
	@RequestMapping(value = "/login_ok.action", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView login_ok(ThorClientDTO dto, HttpServletRequest request) throws Exception{
		
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();

		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		
		dto = thorService.getReadData(id);
		
		if(dto==null || (!dto.getPwd().equals(pwd))){
			
			request.setAttribute("message", "아이디 또는 패스워드를 정확히입력하세요");
			
			mav.setViewName("thorcar/login");
			return mav;
			
		}
		
		LoginDTO l_dto = new LoginDTO();
		
		l_dto.setId(dto.getId());
		l_dto.setName(dto.getName());
		l_dto.setTel(dto.getTel());
		l_dto.setLi_num(dto.getLi_num());
		l_dto.setNum(dto.getNum());
		l_dto.setType(dto.getType());
		l_dto.setBirth(dto.getBirth());
		
		session.setAttribute("loginDTO", l_dto);
		
		mav.setViewName("redirect:/main.action");		
		return mav;
		
	}
	
	@RequestMapping(value = "/searchId.action",method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView searchId() throws Exception{
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("thorcar/searchId");
		return mav;
		
	}
	
	@RequestMapping(value = "/searchId_ok.action",method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView searchId_ok(ThorClientDTO dto, HttpServletRequest request) throws Exception{
		
		ModelAndView mav = new ModelAndView();
		
		String name = request.getParameter("name");
		String tel = request.getParameter("tel");
		
		dto = thorService.getReadTel(tel);
		
		if(dto==null || (!dto.getName().equals(name))) {
			
			request.setAttribute("message", "아이디 또는 전화번호가 올바르지 않습니다");
			request.setAttribute("head_message", "회원정보가 확인되지 않습니다. 다시 입력해주세요!");
			
			mav.setViewName("thorcar/idResult");
			return mav;
		}
		if(dto!=null && (dto.getName().equals(name))) {
			
			request.setAttribute("message", "회원님의 아이디는 ["+ dto.getId() + "] 입니다");
			request.setAttribute("head_message", "회원님의 휴대전화로 가입된 회원정보가 있습니다");
			
			mav.setViewName("thorcar/idResult");
			return mav;
		} 
		
		mav.setViewName("redirect:/login.action");
		return mav;
	}
	
	@RequestMapping(value = "/searchPwd.action",method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView searchPwd() throws Exception{
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("thorcar/searchPwd");
		return mav;
		
	}	
	
	@RequestMapping(value = "/searchPwd_ok.action",method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView searchPwd_ok(HttpServletRequest request) throws Exception{
		
		ModelAndView mav = new ModelAndView();

		String id = request.getParameter("id");
        String tel = request.getParameter("tel");
        
        ThorClientDTO dto = thorService.getReadTel(tel);

        if(dto==null || (!dto.getId().equals(id))) {
			
			request.setAttribute("message", "아이디 또는 전화번호가 올바르지 않습니다");
			request.setAttribute("head_message", "회원정보가 확인되지 않습니다. 다시 입력해주세요!");
			
			mav.setViewName("thorcar/pwd_fail");
			return mav;
		}
		
        if(dto!=null && (dto.getId().equals(id))) {

			request.setAttribute("head_message", dto.getId() + " 님의 정보가 인증되었습니다. 비밀번호를 변경해주세요");
			mav.addObject("dto",dto);
			mav.setViewName("thorcar/changePwd");
            return mav;
        } 

        mav.setViewName("redirect:/login.action");
        return mav;
		
	}
	
	@RequestMapping(value = "/changePwd.action",method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView changePwd(HttpServletRequest request) throws Exception{
		
		String id = request.getParameter("id");
		
		ThorClientDTO dto = thorService.getReadData(id);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("dto", dto);
		
		mav.setViewName("thorcar/changePwd");
		return mav;
		
	}
	
	@RequestMapping(value = "/update_Pwd.action",method = {RequestMethod.POST})
	public ModelAndView update_Pwd(ThorClientDTO dto,HttpServletRequest request) throws Exception{
		
		ModelAndView mav = new ModelAndView();

        thorService.updatePwd(dto);

        mav.setViewName("redirect:/login.action");
        return mav;
		
	}
	
	@RequestMapping(value = "/idResult.action",method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView idResult() throws Exception{
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("thorcar/idResult");
		return mav;
		
	}
	
	@RequestMapping(value = "/logout.action",method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView logout(HttpServletRequest request) throws Exception{
		
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView();

		session.removeAttribute("loginDTO");
		session.invalidate();
		
		mav.setViewName("redirect:/main.action");
		return mav;
		
	}
	
	@RequestMapping(value = "/signup.action",method = RequestMethod.GET)
	public ModelAndView signup() throws Exception{
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("thorcar/signup");
		return mav;
		
	}
	
	@RequestMapping(value = "/signup.action",method = {RequestMethod.POST})
    public ModelAndView signup_ok(ThorClientDTO dto, HttpServletRequest request, HttpServletResponse response) throws Exception{

        ModelAndView mav = new ModelAndView();
        
        int result = thorService.idCheck(dto);
        int result2 = thorService.telCheck(dto);
        
        try {
        	if(result == 1 || result2 == 1) {	
        		response.setContentType("text/html; charset=UTF-8");
        		 
        		PrintWriter out = response.getWriter();
        		 
        		out.println("<script>alert('회원가입 실패! 정보를 다시 입력해주세요'); location.href='/signup.action';</script>");
        		 
        		out.flush();

        		mav.setViewName("thorcar/signup");

        		return mav;
        	}else if (result==0 && result2 == 0) {
	        	int maxNum = thorService.maxNum();
	            
	            dto.setNum(maxNum + 1);
	            dto.setName(request.getParameter("name"));
	            dto.setTel(request.getParameter("tel"));
	            dto.setId(request.getParameter("id"));
	            dto.setPwd(request.getParameter("pwd"));
	            dto.setLi_num(request.getParameter("li_num"));
	            
	            thorService.insertData(dto);
	            
	            response.setContentType("text/html; charset=UTF-8");
       		 
        		PrintWriter out = response.getWriter();
        		 
        		out.println("<script>alert('회원가입을 축하드립니다'); location.href='/login.action';</script>");
        		 
        		out.flush();

	            mav.setViewName("thorcar/login");
	    		return mav;
        	
        	}
        	
        }catch (Exception e) {
        	throw new RuntimeException();
        }
        
        mav.setViewName("redirect:/main.action");
        return mav;

    }
	
	@ResponseBody
	@RequestMapping(value = "/idCheck.action",method = {RequestMethod.POST})
    public int idCheck(ThorClientDTO dto) throws Exception{

        int result = thorService.idCheck(dto);
        return result;

    }
	
	@ResponseBody
	@RequestMapping(value = "/telCheck.action",method = {RequestMethod.POST})
    public int telCheck(ThorClientDTO dto) throws Exception{

        int result = thorService.telCheck(dto);
        return result;

    }
	
	@RequestMapping(value = "/carInfo.action", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView carInfo(HttpServletRequest request, ThorCarDTO dto) throws Exception{
		
			int num = Integer.parseInt(request.getParameter("num"));
			dto = thorService.getReadData1(num);
			HttpSession session = request.getSession();
			
			session.setAttribute("ThorCarDTO", dto);
			ModelAndView mav = new ModelAndView();
			
			
			
			mav.setViewName("thorcar/carInfo");
			return mav;
	}
	
	@RequestMapping(value = "/searchCar.action",method = {RequestMethod.GET, RequestMethod.POST})
	   public ModelAndView searchCar(ThorCarDTO dto, HttpServletRequest request) throws Exception{
		
		String searchValue = request.getParameter("searchValue");

		
		if(searchValue!=null&&!searchValue.equals("")) {
			
			if(request.getMethod().equalsIgnoreCase("GET")) {
				searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}
			
		}else {
			searchValue = "";
		}
		
	     ModelAndView mav = new ModelAndView();
	      
	     List<ThorCarDTO> lists = thorService.getListsFromSearchValue(searchValue);
	     
	     
	     mav.addObject("lists", lists);
	     mav.setViewName("thorcar/searchCar");
	     return mav;
	      
	   }
	
	@RequestMapping(value = "/searchCar_ok.action",method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView searchCar_ok(HttpServletRequest request) throws Exception{

        ModelAndView mav = new ModelAndView();
        String indate = request.getParameter("checkin");
        String outdate = request.getParameter("checkout");
        String staydate = request.getParameter("days");
        
        HttpSession session = request.getSession();

        MainDateDTO md_dto = new MainDateDTO();
        
        md_dto.setInDate(indate);
        md_dto.setOutDate(outdate);
        md_dto.setStaydate(staydate);
        
        session.setAttribute("MainDateDTO", md_dto);
        mav.setViewName("redirect:/searchCar.action");
        return mav;
    }
	
	@RequestMapping(value = "/child.action",method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView child(HttpServletRequest request) throws Exception{
      request.setCharacterEncoding("UTF-8");
       
       List<LentDTO> lists = lentService.getLists();
       
       ModelAndView mav = new ModelAndView();
       
       mav.addObject("lists", lists);

        mav.setViewName("thorcar/child");
        return mav;
    }
	
	@RequestMapping(value = "/cscenter.action",method = RequestMethod.GET)
	public ModelAndView cscenter() throws Exception{
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("thorcar/cscenter");
		return mav;
	}
	
	@RequestMapping(value = "/pay.action",method = RequestMethod.GET)
	public ModelAndView pay(ThorClientDTO dto) throws Exception{
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("thorcar/pay");
		return mav;
	}
	
	@RequestMapping(value = "/subPay.action",method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView subPay(LoginDTO dto, HttpServletRequest request, HttpServletResponse response) throws Exception{

		ModelAndView mav = new ModelAndView();
		
		HttpSession session = request.getSession();
		dto = (LoginDTO)session.getAttribute("loginDTO");
		
		//System.out.println(dto);

		
		if(dto==null) {
			
			response.setContentType("text/html; charset=UTF-8");
       		 
     		PrintWriter out = response.getWriter();
     		 
     		out.println("<script>alert('로그인 후 이용가능한 서비스입니다.');</script>");
     		 
     		out.flush();

            mav.setViewName("thorcar/subInfo");
    		return mav;
	
		}else {
			
			mav.setViewName("thorcar/subPay");
			return mav;
		}
		
	}
	
	

	
}