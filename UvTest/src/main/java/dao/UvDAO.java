package dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import vo.UvVO;

public class UvDAO {

	SqlSession sqlSession;
	
	public UvDAO(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	//DB에 현재 날짜 유무 확인
	public List<UvVO> uv_list_date(String dateDb){
		List<UvVO> list = sqlSession.selectList("uv.uv_list_date", dateDb);
		return list;
	} 
	
	//DB 전체조회
	public List<UvVO> selectList(){
		List<UvVO> list = sqlSession.selectList("uv.uv_list");
		return list;
	}
	
	//uv데이터 추가
	public int uv_insert(List<UvVO> uv_list) {
		int res = 0;
		for(UvVO vo : uv_list) {
			res += sqlSession.insert("uv.uv_insert", vo);
		}
		return res;
	}
	
}
