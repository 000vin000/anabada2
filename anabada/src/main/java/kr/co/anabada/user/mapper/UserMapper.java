package kr.co.anabada.user.mapper;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.co.anabada.user.entity.User;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO user (userId, userPw, userName, userNick, userAdd, userEmail, userPhone) " +
            "VALUES (#{userId}, #{userPw}, #{userName}, #{userNick}, #{userAdd}, #{userEmail}, #{userPhone})")
    void insertUser(User user);

    @Select("SELECT * FROM user WHERE userId = #{userId}")
    User selectByUserId(String userId);

    @Select("SELECT * FROM user WHERE userNick = #{userNick}")
    User selectByUserNick(String userNick);

    @Select("SELECT * FROM user WHERE userEmail = #{userEmail}")
    User selectByUserEmail(String userEmail);
    
    @Select("SELECT * FROM user WHERE userPhone = #{userPhone}")
    User selectByUserPhone(String userPhone);
    
    @Update("UPDATE user SET userPw = #{userPw}, userName = #{userName}, userNick = #{userNick}, userAdd = #{userAdd}, userEmail = #{userEmail}, userPhone = #{userPhone} WHERE userId = #{userId}")
    void updateUser(User user);
    
    @Select("SELECT * FROM user WHERE userNo = #{userNo}")
    User selectByUserNo(int userNo); // jhu
    
    @Select("SELECT userNick FROM user WHERE userNo = #{userNo}")
    String selectUserNick(int userNo);

    //회원 탈퇴기능
    @Update("UPDATE alluser SET userStatus = #{userStatus}, userDeactiveDate = #{userDeactiveDate} WHERE userNo = #{userNo}")
    void updateUserStatus(@Param("userNo") int userNo, @Param("userStatus") String userStatus, @Param("userDeactiveDate") LocalDateTime userDeactiveDate);
    
    @Select("SELECT * FROM alluser WHERE userNo = #{userNo}")
    User selectAllUserByUserNo(@Param("userNo") int userNo);
    
      
}
