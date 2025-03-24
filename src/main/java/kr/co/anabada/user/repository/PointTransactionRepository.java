package kr.co.anabada.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kr.co.anabada.user.entity.Point_Transaction;

@Repository
public interface PointTransactionRepository extends JpaRepository<Point_Transaction, Integer> {

    // charge 타입의 포인트 총합 계산
    @Query("SELECT SUM(p.pointTrAmount) FROM Point_Transaction p WHERE p.pointTrType = 'charge'")
    Double getTotalChargeAmount();

    // use 타입의 포인트 총합 계산
    @Query("SELECT SUM(p.pointTrAmount) FROM Point_Transaction p WHERE p.pointTrType = 'use'")
    Double getTotalUseAmount();

    // refund 타입의 포인트 총합 계산
    @Query("SELECT SUM(p.pointTrAmount) FROM Point_Transaction p WHERE p.pointTrType = 'refund'")
    Double getTotalRefundAmount();
}

