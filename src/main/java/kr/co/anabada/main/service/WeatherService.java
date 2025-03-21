package kr.co.anabada.main.service;

import java.util.Arrays;
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
	public List<ItemInclude1Image> getTempList(String temp) {
		String tempLevel = getTempLevel(temp);

		// 범위별 카테고리 목록
		List<String> cateList = repo.getCateNoByTemp(tempLevel);
		
		return repo.getItemsByTemp(cateList);
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
}
