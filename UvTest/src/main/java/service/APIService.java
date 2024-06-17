package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class APIService {
	
	public Map<String, Object> getJson(Double lat, Double lon) throws IOException{
		
		//JSON 데이터
		String urlStr = String.format("https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&forecast_days=3&hourly=direct_radiation",
				lat, lon);
		
		URL url = new URL(urlStr); //IOException
		HttpURLConnection connection = (HttpURLConnection)url.openConnection(); //IOException
		BufferedReader br = new BufferedReader(
				new InputStreamReader(connection.getInputStream()));
		
		String line = "";
		String resultJson = ""; 
		
		while((line = br.readLine()) != null) {
			resultJson += line;
		}
		System.out.println("radi Json : " + resultJson);
		br.close(); connection.disconnect();
		
		ObjectMapper om = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			JsonNode rootNode = om.readTree(resultJson);
			JsonNode timeNode = rootNode.path("hourly").path("time");
			JsonNode radiNode = rootNode.path("hourly").path("direct_radiation");
			
			List<String> timelist = om.readValue(timeNode.toString(), new TypeReference<List<String>>() {});
			List<String> radilist = om.readValue(radiNode.toString(), new TypeReference<List<String>>() {});
			
			map.put("time", timelist);
			map.put("radi", radilist);
			
		} catch (Exception e) {
			System.out.println("예외 오류");
			// TODO: handle exception
		}
		
		return map;
	}
}
