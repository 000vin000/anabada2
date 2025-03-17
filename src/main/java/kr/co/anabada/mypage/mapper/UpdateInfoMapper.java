package kr.co.anabada.mypage.mapper;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UpdateInfoMapper {

    @Update("UPDATE alluser SET userStatus = 'deactive', userDeactiveDate = #{deactiveDate} WHERE userNo = #{userNo}")
    int deactivateUser(@Param("userNo") int userNo, @Param("deactiveDate") LocalDateTime deactiveDate);
}

