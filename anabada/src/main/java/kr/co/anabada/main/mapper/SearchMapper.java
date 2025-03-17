package kr.co.anabada.main.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.entity.ItemImage;

@Mapper
public interface SearchMapper {
	// 상품명 검색
	@Select("SELECT * FROM itemInclude WHERE itemName LIKE CONCAT('%', #{keyword}, '%')")
    List<ItemImage> selectByItemName(String keyword);
	
	// 닉네임 검색
	@Select("SELECT * FROM itemInclude WHERE userNo = (SELECT userNo FROM user WHERE userNick = #{userNick})")
	List<ItemImage> selectByUserName(String userNick);
}
