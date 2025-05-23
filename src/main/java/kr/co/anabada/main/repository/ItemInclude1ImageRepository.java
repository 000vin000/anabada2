package kr.co.anabada.main.repository;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.co.anabada.main.dto.ItemInclude1Image;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ItemInclude1ImageRepository {
    private final JdbcTemplate jdbcTemplate;

    // 전체 데이터 조회
    public List<ItemInclude1Image> findAllItems() {
        String sql = "SELECT * FROM item_include_1image";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ItemInclude1Image.class));
    }

    // 키워드로 검색
    public List<ItemInclude1Image> findSearchItems(String findType, String keyword) {
    	if (findType == null || findType.trim().isEmpty()) {
    		return null;
    	}
    	
        String sql = "SELECT * FROM item_include_1image WHERE ";

        if (findType.equals("itemName")) {
            sql += "item_title LIKE CONCAT('%', '"+ keyword + "', '%')";
        } else if (findType.equals("userNick")) {
            sql += "user_nick = '" + keyword + "'";
        } else {
            return null;
        }

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ItemInclude1Image.class));
    }
    
    // 카테고리 검색 - 성별 전체
    public List<ItemInclude1Image> selectGenderAll(String ct, String cd) {
    	String param = ct + cd;
    	String sql = "SELECT * FROM item_include_1image WHERE category_no LIKE CONCAT('%', '" + param + "')";
    	return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ItemInclude1Image.class));
    }
    
    // 카테고리 검색 - 남성/여성
    public List<ItemInclude1Image> selectGender(String gender, String ct, String cd) {
    	String param = gender + ct + cd;
    	String sql = "SELECT * FROM item_include_1image WHERE category_no LIKE CONCAT('%', '" + param + "')";
    	return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ItemInclude1Image.class));
    }
    
    // 카테고리 이름
    public String detailName(String ct, String cd) {
        String sql = "SELECT DISTINCT category_name FROM item_category WHERE category_no LIKE CONCAT('%', ?, ?, '%')";
        return jdbcTemplate.queryForObject(sql, String.class, ct, cd);
    }

}
