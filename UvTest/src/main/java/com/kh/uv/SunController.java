package com.kh.uv;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import dao.RadiDAO;
import dao.UvDAO;
import service.APIService;
import service.UvApiService;
import util.Commons;
import vo.RadiVO;
import vo.UvVO;

@Controller
public class SunController {

	RadiDAO radi_dao;
	UvDAO uv_dao;
	boolean insert_ok = false;
	boolean uvInsert_ok = false;
	
	public SunController(RadiDAO radi_dao, UvDAO uv_dao) {
		this.radi_dao = radi_dao;
		this.uv_dao = uv_dao;
	} 
	
	@RequestMapping(value= {"/", "list.do"}) 
	public String list(Model model){ 
		
		//서울 시각
		ZonedDateTime seoulTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")); 
		System.out.println("지금 서울 시각 : " + seoulTime);
		//시간 형식 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedNow = formatter.format(seoulTime);
		System.out.println("형식변환 된 서울 시각 : " + formattedNow);
		//DB에 해당 날짜 유무 확인
		List<RadiVO> list = radi_dao.radi_list_date(formattedNow);
		
		//존재할 경우 전체 리스트 조회
		if(list.size() > 0) {
			List<RadiVO> total_list = radi_dao.selectList();
			model.addAttribute("list", total_list);
			return Commons.SunCommon.VIEW_PATH + "radi_list.jsp";
			
		}else if(!insert_ok) { //DB에 날짜 없으면 insert작업
			return "redirect:insert.do";
			
		}else { //insert 후에도 DB에 데이터가 없을 때
			model.addAttribute("no_date", "데이터 없음");
			return Commons.SunCommon.VIEW_PATH + "radi_list.jsp";
		}
	}
	
	//APIService 호출하여 DB에 insert
	@RequestMapping("insert.do")
	public String insert() throws IOException { //IOException (APIService에서 던짐)
		APIService serv = new APIService();
		Map<String, Object> map = serv.getJson(37.5519, 126.9918);
		radi_dao.insert(map);
		insert_ok = true;
		
		return "redirect:list.do"; 
	}
	
//주소변경**	
	@RequestMapping("/uv_list.do")
	public String change(Model model) {
		List<UvVO> list = uv_dao.selectList();
		model.addAttribute("list", list);
		
		return Commons.SunCommon.VIEW_PATH_UV + "uv_bar.jsp";
	}
	
	//uv insert
	@RequestMapping("uv_change.do")
	public String uv_change(Model model) {
		//현재시각
		ZonedDateTime seoulTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
		//시간포맷팅
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String dt = formatter.format(seoulTime);
		System.out.println("uv 현재시각 : " + dt);
		
//추가***        
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateDb = formatter.format(seoulTime);
        
        List<UvVO> list = uv_dao.uv_list_date(dateDb);
        
        if(list.size() > 0) {
        	List<UvVO> total_list = uv_dao.selectList();
        	model.addAttribute("list", total_list);
        	return Commons.SunCommon.VIEW_PATH_UV + "uv_bar.jsp";
        	
        }else if(!uvInsert_ok) { 
        	model.addAttribute(dt);
    		model.addAttribute("lat", 37.5519);
    		model.addAttribute("lng", 126.9918);
    		
    		return Commons.SunCommon.VIEW_PATH_UV + "uv_test.jsp"; 
        }else {
        	model.addAttribute("no_date", "데이터 없음");
        	return Commons.SunCommon.VIEW_PATH_UV + "uv_bar.jsp";
        }
//**
	}
	
//주소변경**
	@RequestMapping("uv_insert.do")
	public String uvInsert(String result) {
		//JSON문자열 파싱
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = null;
		JSONArray arr_data = null;
		
		try {
			jsonObject = (JSONObject)parser.parse(result);
			arr_data = (JSONArray)jsonObject.get("result");
			
			System.out.println("uv json데이터 : " + jsonObject);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		UvApiService uvservice = new UvApiService();
		List<UvVO> uv_list = uvservice.get_uv(arr_data);
		int res = uv_dao.uv_insert(uv_list);
		System.out.println("insert완료 : " + res);
		
		return "redirect:uv_list.do"; 
	}
}
