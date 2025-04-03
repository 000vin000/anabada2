package kr.co.anabada.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kr.co.anabada.coin.entity.Conversion;

@Repository
public interface AcceptConversionRepository extends JpaRepository<Conversion, Integer> {
    @Query("SELECT c FROM Conversion c WHERE c.adminNo IS NULL AND c.conversionAt IS NULL")
    List<Conversion> getConList();
}
