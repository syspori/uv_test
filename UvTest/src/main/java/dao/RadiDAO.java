package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import vo.RadiVO;

public class RadiDAO {

	SqlSession sqlSession;
	
	public RadiDAO(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	//현재 날짜 DB에 있는지 확인
	public List<RadiVO> radi_list_date(String date){
		List<RadiVO> list = sqlSession.selectList("radi.radi_list_date", date);
		return list;
	}
	
	//전체 조회
	public List<RadiVO> selectList(){
		List<RadiVO> list = sqlSession.selectList("radi.radi_list");
		return list; 
	}
	
	//radi DB추가
	public int insert(Map<String, Object> map) {
		
		Map<String, String> db_map = new HashMap<String, String>();
		List<String> timelist = (List<String>)map.get("time");
		List<String> radilist = (List<String>)map.get("radi");
		
		int res = 0;
		for(int i = 0; i < timelist.size(); i++) {
			db_map.put("time", timelist.get(i));
			db_map.put("radi", radilist.get(i));
			res += sqlSession.insert("radi.insert", map);
		}

		return res;
	}
}
