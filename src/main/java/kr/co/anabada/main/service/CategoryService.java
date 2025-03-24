package kr.co.anabada.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.main.GenderNotFoundException;
import kr.co.anabada.main.mapper.CategoryMapper;

@Service
public class CategoryService {
	@Autowired
	private CategoryMapper mapper;
	
	// level 2
	public String getGenderName(String gender) {
		String value = null;
		
		if (gender.equals("00")) value =  "전체";
		else value = mapper.getGenderName(gender);
		
		if (value != null && !value.isEmpty()) { return value; }
		else throw new GenderNotFoundException("Gender not found for: " + gender);
	}

	// level 3
	public String getClothesTypeName(String ct) {
		if (ct.equals("01")) return "아우터";
		else if (ct.equals("02")) return "상의";
		else if (ct.equals("03")) return "하의";
		else if (ct.equals("04")) return "원피스";
		else if (ct.equals("05")) return "스커트";
		else if (ct.equals("06")) return "가방";
		else if (ct.equals("07")) return "패션소품";
		else if (ct.equals("08")) return "신발";
		else return "-";
	}

	// level 4
	public String getClothesTypeDetailName(String cd) {
		// TODO Auto-generated method stub
		return null;
	}

}
