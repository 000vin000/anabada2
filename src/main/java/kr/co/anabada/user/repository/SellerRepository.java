package kr.co.anabada.user.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kr.co.anabada.user.entity.Seller;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Integer> {
    // 판매자의 전체 판매액 합계 구하기
	@Query("SELECT SUM(s.sellerTotalSales) FROM Seller s")
    BigDecimal sumTotalSales();
}