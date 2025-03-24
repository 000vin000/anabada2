package kr.co.anabada.main.repository;

import java.util.Collections;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.co.anabada.main.dto.ItemInclude1Image;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class WeatherRepository {
	private final JdbcTemplate jdbcTemplate;
	
	// 온도 범위별 카테고리 조회
	@SuppressWarnings("deprecation")
	public List<String> getCateNoByTemp(String tempLevel) {
	    String sql = "SELECT category_no FROM item_category "
	                + "WHERE category_temperature LIKE CONCAT('%', ?, '%') ";	            

	    return jdbcTemplate.query(sql, new Object[]{tempLevel}, (rs, rowNum) -> rs.getString("category_no"));
	}


	// 온도에 맞는 아이템 조회
	@SuppressWarnings("deprecation")
	public List<ItemInclude1Image> getItemsByTemp(List<String> cateList) {
		String placeholders = String.join(",", Collections.nCopies(cateList.size(), "?"));
	    String sql = "SELECT * FROM item_include_1image WHERE category_no IN (" + placeholders + ")";

	    return jdbcTemplate.query(sql, cateList.toArray(), new BeanPropertyRowMapper<>(ItemInclude1Image.class));
	}
}
