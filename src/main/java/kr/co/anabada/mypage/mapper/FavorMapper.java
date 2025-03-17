package kr.co.anabada.mypage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import kr.co.anabada.mypage.entity.Favor;

@Mapper
public interface FavorMapper {
	@Select("SELECT * FROM favor WHERE userNo = #{userNo} ORDER BY favorNo DESC")
	List<Favor> selectMyFavor(int userNo);
	
	@Select("SELECT * FROM favor WHERE userNo = #{userNo} AND itemNo = #{itemNo}")
	Favor selectMyFavorItem(@Param("userNo") int userNo, @Param("itemNo") int itemNo);
	
	@Insert("INSERT INTO favor (userNo, itemNo) VALUES (#{userNo}, #{itemNo})")
	int addFavor(@Param("userNo") int userNo, @Param("itemNo") int itemNo);
	
	@Delete("DELETE FROM favor WHERE userNo = #{userNo} AND itemNo = #{itemNo}")
	int removeFavor(@Param("userNo") int userNo, @Param("itemNo") int itemNo);

	@Select("SELECT count(*) FROM favor WHERE userNo = #{userNo} AND itemNo = #{itemNo}")
	boolean isFavor(int userNo, int itemNo);
}
