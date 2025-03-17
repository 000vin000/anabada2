package kr.co.anabada.main.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.entity.ItemImage;

@Mapper
public interface CategoryMapper {
	// 성별&옷 종류 검색
	@Select("SELECT * FROM iteminclude WHERE itemGender = #{itemGender} "
					+ "AND itemCate = #{itemCate}")
	List<ItemImage> selectByGenderAndCate(String itemGender, String itemCate);

	// 성별&전체 검색
	@Select("SELECT * FROM iteminclude WHERE itemGender = #{itemGender}")
	List<ItemImage> searchGender(String itemGender);

	// 전체&옷 종류 검색
	@Select("SELECT * FROM iteminclude WHERE itemCate = #{itemCate}")
	List<ItemImage> searchClothesType(String itemCate);
}
