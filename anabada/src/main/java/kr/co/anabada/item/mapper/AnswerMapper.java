package kr.co.anabada.item.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.co.anabada.item.entity.Answer;
import kr.co.anabada.item.entity.QnA;

@Mapper
public interface AnswerMapper {
    @Insert("INSERT INTO answer (qNo, userNo, aContent, aDate) " +
            "VALUES (#{qNo}, #{userNo}, #{aContent}, #{aDate})")
    void insertA(int qNo, int userNo, String aContent, LocalDateTime aDate);
    
    //내가 받은 문의 목록
    @Select("SELECT i.itemName, i.itemNo, qUser.userNick, " +           
            "q.qNo, q.qTitle, q.qContent, q.qDate, a.aContent, a.aDate " +           
            "FROM question q " +
            "LEFT JOIN answer a ON q.qNo = a.qNo " + 
            "LEFT JOIN item i ON q.itemNo = i.itemNo " +
            "JOIN user qUser ON q.userNo = qUser.userNo " +
            "JOIN user iUser ON i.userNo = iUser.userNo " +
            "WHERE iUser.userNo = #{userNo}")
    List<QnA> getAList(int userNo);

    @Update("UPDATE answer SET aContent = #{aContent}, aDate = NOW() WHERE aNo = #{aNo}")
    void updateA(Answer a);

    @Delete("DELETE FROM answer WHERE aNo = #{aNo}")
    void deleteA(int aNo);





}
