package kr.co.anabada.coin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kr.co.anabada.coin.entity.Conversion;
import kr.co.anabada.user.entity.User;

@Repository
public interface ConversionRepository extends JpaRepository<Conversion, Integer> {
	@Query("SELECT c FROM Conversion c WHERE c.userNo = :userNo AND c.conversionType = 'TOCOIN'")
    List<Conversion> toCoinfindByUserNo(User userNo);
}
