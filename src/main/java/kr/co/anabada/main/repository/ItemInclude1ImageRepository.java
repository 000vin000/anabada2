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

    // 특정 아이템 조회
    public ItemInclude1Image findByItemNo(Integer itemNo) {
        String sql = "SELECT * FROM item_include_1image WHERE item_no = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ItemInclude1Image.class), itemNo);
    }
}
