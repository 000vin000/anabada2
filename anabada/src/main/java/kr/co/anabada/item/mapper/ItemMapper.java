package kr.co.anabada.item.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.entity.ItemImage;

@Mapper
public interface ItemMapper {
	@Insert("INSERT INTO item (userNo, itemGender, itemCate, itemAuction, itemStart, itemEnd, itemPrice, itemName, itemContent, itemStatus) " +
	        "VALUES (#{userNo}, #{itemGender}, #{itemCate}, #{itemAuction}, #{itemStart}, #{itemEnd}, #{itemPrice}, #{itemName}, #{itemContent}, #{itemStatus})")
	@Options(useGeneratedKeys = true, keyProperty = "itemNo", keyColumn = "itemNo")
	int save(Item item);

	
	@Update("UPDATE item SET itemAuction = #{itemAuction} WHERE itemNo = #{itemNo}")
    int updateAuctionStatus(@Param("itemNo") int itemNo, @Param("itemAuction") String itemAuction);

	@Select("SELECT * FROM item WHERE userNo = #{userNo}")
    List<Item> findItemsByUserNo(int userNo);
	
	@Select("""
			SELECT i.*, u.userNick,
				(SELECT COUNT(*) FROM bid WHERE i.itemNo = bid.itemNo) AS bidCount FROM item i
				JOIN user u ON i.userNo = u.userNo
				WHERE i.itemNo = #{itemNo}
			""")
	ItemImage findItemsByItemNo(int itemNo); // jhu

	@Update("UPDATE item " +
	        "SET itemName = #{itemName}, itemStart = #{itemStart}, itemEnd = #{itemEnd}, itemPrice = #{itemPrice}, " +
	        "itemContent = #{itemContent}, itemStatus = #{itemStatus}, itemCate = #{itemCate}, itemAuction = #{itemAuction}, itemGender = #{itemGender}" +
	        " WHERE itemNo = #{itemNo}")
	int updateItem(Item item);
	
	@Select("SELECT * FROM item WHERE itemNo = #{itemNo}")
	Item findItemById(int itemNo); 
	
	@Delete("DELETE FROM item WHERE itemNo = #{itemNo}")
	int deleteItem(@Param("itemNo") int itemNo);
}
