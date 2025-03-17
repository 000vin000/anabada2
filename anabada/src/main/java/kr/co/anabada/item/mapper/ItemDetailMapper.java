package kr.co.anabada.item.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.co.anabada.item.entity.Image;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.entity.Question;

@Mapper
public interface ItemDetailMapper {
	@Select("SELECT * FROM item WHERE itemNo = #{itemNo}")
	Item findItem(int itemNo);

	@Select("SELECT itemPrice FROM item WHERE itemNo = #{itemNo}")
	int getCurrentPrice(int itemNo);

	@Select("SELECT itemAuction FROM item WHERE itemNo = #{itemNo}")
	String getCurrentState(int itemNo);
	
	@Select("SELECT itemStart FROM item WHERE itemNo = #{itemNo}")
	LocalDateTime getItemStart(int itemNo);
	
	@Select("SELECT itemEnd FROM item WHERE itemNo = #{itemNo}")
	LocalDateTime getItemEnd(int itemNo);

	@Select("SELECT * FROM image WHERE itemNo = #{itemNo}")
	List<Image> getAllImages(int itemNo);

	@Update("UPDATE item SET itemPrice = #{itemPrice} WHERE itemNo = #{itemNo}")
	int updatePrice(@Param("itemNo") int itemNo, @Param("itemPrice") int itemPrice);
}
