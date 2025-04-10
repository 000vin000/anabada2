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
		else if (gender.equals("10")) value = "남성";
		else if (gender.equals("20")) value = "여성";
		else return null;
		
		if (value != null && !value.isEmpty()) { return value; }
		else throw new GenderNotFoundException("Gender not found for: " + gender);
	}

	// level 4
	public String getClothesTypeDetailName(String ct, String cd) {
		String cateDetail = ct + cd;
		String value = mapper.findCateDetail(cateDetail);
		
		if (value == null || value.isEmpty()) {
			throw new GenderNotFoundException("ClothesTypeDetail not found for: " + cd);
		}
		
		System.out.println(ct); // 01
		System.out.println(value); // 아우터
		if (ct.equals(value)) value = "전체";
		
		return value;
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
		else throw new GenderNotFoundException("ClothesType not found for: " + ct);
	}
}
