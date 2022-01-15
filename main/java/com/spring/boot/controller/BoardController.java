package com.spring.boot.controller;

import java.net.URLDecoder; 
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.spring.boot.dto.BoardDTO;
import com.spring.boot.mapper.BoardService;
import com.spring.boot.util.MyUtil;



@RestController
public class BoardController {

	@Resource
	private BoardService boardService;
	
	@Autowired
	MyUtil myUtil;
	
	@RequestMapping(value = "/myPage.action",method = RequestMethod.GET)
	public ModelAndView created(HttpServletRequest request) throws Exception{
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("thorcar/myPage");
		return mav;
		
	}
	
	
	
	@RequestMapping(value = "qnaCreated.action",method = RequestMethod.GET)
	public ModelAndView created() throws Exception{
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("thorcar/qnaCreated");

		return mav;
	}
	
	@RequestMapping(value = "created_ok.action",method = RequestMethod.POST)
	public ModelAndView created_ok(BoardDTO dto,HttpServletRequest request) 
			throws Exception{
		
		ModelAndView mav = new ModelAndView();
		
		int maxNum = boardService.maxNum();
		
		dto.setBoardNum(maxNum+1);
		dto.setIpAddr(request.getRemoteAddr());
		
		boardService.insertData(dto);
		
		mav.setViewName("redirect:/qna.action");

		return mav;
	}
	
	@RequestMapping(value = "qnarply.action",method = RequestMethod.GET)
	public ModelAndView reply() throws Exception{
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("thorcar/qnaCreated");

		return mav;
	}
	
	@RequestMapping(value = "reply_ok.action",method = RequestMethod.POST)
	public ModelAndView reply_ok(BoardDTO dto,HttpServletRequest request) 
			throws Exception{
		
		ModelAndView mav = new ModelAndView();
		
		int maxNum = boardService.maxNum();
		
		dto.setBoardNum(maxNum+1);
		dto.setIpAddr(request.getRemoteAddr());
		
		boardService.insertData(dto);
		
		mav.setViewName("redirect:/qna.action");

		return mav;
	}
	
	@RequestMapping(value = "qna.action",
			method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView list(BoardDTO dto,HttpServletRequest request) 
			throws Exception{
		
		String pageNum = request.getParameter("pageNum");
 	    
	 	   int currentPage = 1;
	 	   
	 	   if(pageNum!=null) {
	 		  currentPage = Integer.parseInt(pageNum);
	 	   }
	 	   
	 	   
	 	   String searchKey = request.getParameter("searchKey");
	 	   String searchValue = request.getParameter("searchValue");
	 	   
	 	   if(searchValue!= null){
	 		   
	 		  
	 		   if(request.getMethod().equalsIgnoreCase("GET")){
	 			   searchValue = URLDecoder.decode(searchValue, "UTF-8");
	 		   }
	 		   
	 		   
	 	   }else{
	 		   searchKey = "subject";
	 		   searchValue = "";
	 		   
	 	   }
	 	   
	 	   
	 	   
	 	   int dataCount = boardService.getDataCount(searchKey, searchValue);
	 	   
	 	   int numPerPage = 10;
	 	   
	 	   int totalPage = myUtil.getPageCount(numPerPage, dataCount); //totalpage=12
	 	   
	 	   if(currentPage>totalPage){
	 		   currentPage=totalPage;
	 	   }
	 	   
	 	   int start = (currentPage-1)* numPerPage+1; //numperpage:3
	 	   int end = currentPage*numPerPage;
	 	   
	 	   
	 	   List<BoardDTO> lists = 
	 			   boardService.getLists(start,end,searchKey,searchValue); 
	 	   
	 	    String param = "";
	 	   if(!searchValue.equals("")){
	 		   
	 		   param = "?searchKey=" + searchKey;
	 		   param+= "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
	 		   
	 	   }
	 	    
	 	   String listUrl ="/qna.action" + param;
	 	   String pageIndexList = 
	 			   myUtil.pageIndexList(currentPage, totalPage, listUrl);
	 	   
	 	    
	 	   String articleUrl ="/QnaArticle.action";
	 	   
	 	   if(param.equals("")){
	 		   articleUrl += "?pageNum=" + currentPage;
	 	   }else{
	 		   articleUrl += param + "&pageNum=" + currentPage;
	 	   }
	 	   
 	    ModelAndView mav = new ModelAndView();
	 	   
 	   	mav.addObject("lists", lists);
 	   	mav.addObject("pageIndexList", pageIndexList);
 	   	mav.addObject("dataCount", dataCount);
 	   	mav.addObject("articleUrl", articleUrl);
	
		mav.setViewName("thorcar/qna");
		return mav;
		
	}
	
	@RequestMapping(value = "QnaArticle.action",
			method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView article(BoardDTO dto,HttpServletRequest request) 
			throws Exception{
		
		int boardNum = Integer.parseInt(request.getParameter("boardNum"));
 		String pageNum = request.getParameter("pageNum");
 		
 		String searchKey = request.getParameter("searchKey");
 		String searchValue = request.getParameter("searchValue");
 		
 		if(searchValue!=null) {
 			searchValue = URLDecoder.decode(searchValue, "UTF-8");
 		}
 		
 		boardService.updateHitCount(boardNum);
 		
 		dto = boardService.getReadData(boardNum);
 		
 		if(dto==null){
 			
 			ModelAndView mav = new ModelAndView();
 	 		
 	 		mav.setViewName("redirect:/qna.action?pageNum=" + pageNum);
 	 		
 			return mav;
 		}
 		
 		int lineSu = dto.getContent().split("\n").length;
 	    
 		dto.setContent(dto.getContent().replaceAll("\n","<br/>"));
 		
 		
 		
 		String param = "pageNum=" + pageNum;
 		
 		if(searchValue!=null){
 			param+= "&searchKey=" + searchKey;
 			param+= "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
 		}
 		
 		/*
 		request.setAttribute("dto", dto);
 		request.setAttribute("params", param);
 		request.setAttribute("lineSu", lineSu);
 		request.setAttribute("pageNum", pageNum);
		
 		return "bbs/article";
		*/
 		
 		ModelAndView mav = new ModelAndView();
 		
 		mav.setViewName("thorcar/QnaArticle");
 		
 		mav.addObject("dto", dto);
 		mav.addObject("params", param);
 		mav.addObject("lineSu", lineSu);
 		mav.addObject("pageNum", pageNum);
 		
 		
 		return mav;
		
	}
	
	@RequestMapping(value = "qnaUpdated.action",
			method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView updated(BoardDTO dto,HttpServletRequest request) 
			throws Exception{
		
		int boardNum = Integer.parseInt(request.getParameter("boardNum"));
 		String pageNum = request.getParameter("pageNum");
 		
 		String searchKey = request.getParameter("searchKey");
 		String searchValue = request.getParameter("searchValue");
 		
 		if(searchValue!=null) {
 			searchValue = URLDecoder.decode(searchValue, "UTF-8");
 		}
 		
 		dto = boardService.getReadData(boardNum);
 		
 		if(dto==null) {
 			
 			ModelAndView mav = new ModelAndView();
 			mav.setViewName("redirect:/qna.action?pageNum="+pageNum);
 			return mav;
 		}
 		
 		String param = "pageNum=" + pageNum;
 		
 		if(searchValue!=null) {
 			param += "&searchKey=" + searchKey +
 					"&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
 		}
 		
 		ModelAndView mav = new ModelAndView();
 		
 		mav.addObject("dto", dto);
 		mav.addObject("pageNum", pageNum);
 		mav.addObject("params", param);
 		mav.addObject("searchKey", searchKey);
 		mav.addObject("searchValue", searchValue);
		
		
		mav.setViewName("thorcar/qnaUpdated");
		
		return mav;
		
	}
	
	@RequestMapping(value = "/updated_ok.action",
			method = RequestMethod.POST)
	public ModelAndView updated_ok(BoardDTO dto,HttpServletRequest request) 
			throws Exception{
		
		String pageNum = request.getParameter("pageNum");
 		String searchKey = request.getParameter("searchKey");
 		String searchValue = request.getParameter("searchValue");
 	
		boardService.updateData(dto);
		
        String param = "pageNum=" + pageNum;
 		
 		if(!searchValue.equals("")) {
 			param+= "&searchKey=" + searchKey +
 			 "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); 
 	}
 		
 		ModelAndView mav = new ModelAndView();
 		
 		mav.setViewName("redirect:/qna.action?" + param);
 		
 		return mav;
		
	}
	
	@RequestMapping(value = "/deleted.action",
			method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView deleted(BoardDTO dto,HttpServletRequest request) 
			throws Exception{
		
		int boardNum = Integer.parseInt(request.getParameter("boardNum"));
 		String pageNum = request.getParameter("pageNum");
 		
 		String searchKey = request.getParameter("searchKey");
 		String searchValue = request.getParameter("searchValue");
 		
 		boardService.deleteData(boardNum);
 		
 		String param = "pageNum=" + pageNum;
	 		
	 	if(searchValue!=null&&!searchValue.equals("")) {
	 			param+= "&searchKey=" + searchKey +
	 			 "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); 
		
	 	}
		
	 	ModelAndView mav = new ModelAndView();
 		
 		mav.setViewName("redirect:/qna.action?" + param);
 		
 		return mav;
	 		
	 		
	}
	
	@RequestMapping(value = "charging.action",method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView charge() throws Exception{
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("thorcar/chargingPlace");

		return mav;
	}
	
}
