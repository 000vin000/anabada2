package kr.co.anabada.main.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CategoryMapper {
	@Select("SELECT category_name FROM item_category WHERE category_no LIKE CONCAT('%', #{gender}, '%')")
	String getGenderName(String gender);
}
