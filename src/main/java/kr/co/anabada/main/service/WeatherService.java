package kr.co.anabada.main.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.main.dto.ItemInclude1Image;
import kr.co.anabada.main.repository.WeatherRepository;

@Service
public class WeatherService {
	@Autowired
	private WeatherRepository repo;
	
	// 온도별 아이템 리스트 가져오기
	public List<ItemInclude1Image> getTempList(String temp, String lat, String lon) {
		String tempLevel = getTempLevel(temp);

		Double latitude = Double.valueOf(lat); 
		Double longitude = Double.valueOf(lon);
		
		// 범위별 카테고리 목록
		List<String> cateList = repo.getCateNoByTemp(tempLevel);
		
		List<ItemInclude1Image> list =  repo.getItemsByTemp(cateList);
		
		return getIn3km(list, latitude, longitude);
	}

	// 온도 범위
	public String getTempLevel(String temp) {
		try {
	        double tmp = Double.parseDouble(temp); 

	        if (tmp <= 4) return "A";
	        else if (tmp <= 8) return "B";
	        else if (tmp <= 11) return "C";
	        else if (tmp <= 16) return "D";
	        else if (tmp <= 19) return "E";
	        else if (tmp <= 22) return "F";
	        else if (tmp <= 27) return "G";
	        else return "H";
	    } catch (NumberFormatException e) {
	        return "Invalid temperature";  
	    }
	}
	
	// 3km 반경 아이템 리스트
	public List<ItemInclude1Image> getIn3km(List<ItemInclude1Image> list, Double latitude, Double longitude) {
		// 위도, 경도 차이 계산
		double latDiff = 3 / 111.32;
		double lonDiff = 3 / (111.32 * Math.cos(Math.toRadians(latitude)));
		
		// 경계값 설정 (3km 범위)
		double minlat = latitude - latDiff;
		double maxlat = latitude + latDiff;
		double minlon = longitude - lonDiff;
		double maxlon = longitude + lonDiff;
		
		List<ItemInclude1Image> result = new ArrayList<>();
		for (ItemInclude1Image i : list) {
			if (minlat <= i.getItem_latitude() && maxlat >= i.getItem_latitude() && minlon <= i.getItem_longitude() && maxlon >= i.getItem_longitude()) {
				result.add(i);
			}
		}
		return result;
	}

}
