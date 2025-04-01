package kr.co.anabada.coin.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import kr.co.anabada.admin.entity.Admin;
import kr.co.anabada.coin.entity.Conversion;
import kr.co.anabada.user.entity.User;

@Repository
public interface ConversionRepository extends JpaRepository<Conversion, Integer> {
	@Query("SELECT c FROM Conversion c WHERE c.userNo = :userNo AND c.conversionType = 'TOCOIN'")
    List<Conversion> toCoinfindByUserNo(User userNo);

	// 관리자 수락
	@Modifying
	@Transactional
	@Query("UPDATE Conversion c SET c.adminNo = :adminNo, c.conversionAt = :now WHERE c.conversionNo = :conversionNo")
	int updateConversion(Integer conversionNo, Admin adminNo, LocalDateTime now);

}
