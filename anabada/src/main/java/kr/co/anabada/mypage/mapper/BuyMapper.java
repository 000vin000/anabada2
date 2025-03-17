package kr.co.anabada.mypage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import kr.co.anabada.mypage.dto.BuylistDto;

@Mapper
public interface BuyMapper {
	@Select("SELECT * FROM buylist WHERE userNo = #{userNo}")
	List<BuylistDto> selectAllBuy(int userNo);
}
