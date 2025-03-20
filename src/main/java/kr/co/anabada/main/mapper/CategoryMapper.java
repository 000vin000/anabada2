package kr.co.anabada.main.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import kr.co.anabada.main.dto.ItemInclude1Image;

@Mapper
public interface CategoryMapper {
	// 전체 성별
	@Select ("SELECT * FROM item_include_1image WHERE category_no LIKE CONCAT('%', #{ct}, #{cd})")
	List<ItemInclude1Image> selectGenderAll(String ct, String cd);

	// 성별에 따라
	@Select ("SELECT * FROM item_include_1image WHERE category_no LIKE CONCAT('%', #{gender}, #{ct}, #{cd})")
	List<ItemInclude1Image> selectGender(String gender, String ct, String cd);

	@Select ("SELECT distinct category_name FROM item_category WHERE category_no LIKE CONCAT('%', #{ct}, #{cd})")
	String detailName(String ct, String cd);
}
