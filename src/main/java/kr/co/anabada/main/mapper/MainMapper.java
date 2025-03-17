package kr.co.anabada.main.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.core.io.ByteArrayResource;

import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.entity.ItemImage;

@Mapper
public interface MainMapper {
	// 경매중인 전체 상품 정보
	@Select("SELECT * FROM itemInclude")
	List<ItemImage> selectAll();

	// itemNo의 image
	@Select("SELECT imageFile FROM image WHERE itemNo = #{itemNo} LIMIT 1")
	ByteArrayResource selectImage1(int itemNo);
}
