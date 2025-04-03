package kr.co.anabada.admin.repository;

import kr.co.anabada.admin.entity.AdminFee;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminFeeRepository extends JpaRepository<AdminFee, Integer> {
	List<AdminFee> findByAdmincoinAtBetween(LocalDateTime start, LocalDateTime end);

	 @Query("SELECT f FROM AdminFee f WHERE f.admincoinAt BETWEEN :start AND :end")
	    List<AdminFee> findByDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}
