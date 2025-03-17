package kr.co.anabada.item.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import kr.co.anabada.item.entity.Bid;


@Mapper
public interface BidMapper {
	
	@Select("SELECT b.bidtime, b.bidprice, u.userNick FROM bid b " +
	        "JOIN user u ON b.userNo = u.userNo " +
	        "WHERE b.itemNo = #{itemNo} " +
			"ORDER BY b.bidTime DESC")
	List<Bid> getBidList(int itemNo);
	
	@Insert("INSERT INTO bid (itemNo, userNo, bidPrice, bidTime) " +
			"VALUES (#{itemNo}, #{userNo}, #{bidPrice}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "bidNo")
    void insertBid(Bid bid);
	
	
}
