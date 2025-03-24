package kr.co.anabada.item.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.entity.Item.ItemStatus;

@Repository
public interface ItemDetailRepository extends JpaRepository<Item, Integer>{
	@Query("SELECT itemPrice FROM Item WHERE itemNo = :itemNo")
	Optional<BigDecimal> findItemPriceByItemNo(@Param("itemNo") Integer itemNo);
	
	@Query("SELECT itemStatus FROM Item WHERE itemNo = :itemNo")
	Optional<ItemStatus> findItemStatusByItemNo(@Param("itemNo") Integer itemNo);
	
	@Query("SELECT itemSaleStartDate FROM Item WHERE itemNo = :itemNo")
	Optional<LocalDateTime> findItemSaleStartDateByItemNo(@Param("itemNo") Integer itemNo);
	
	@Query("SELECT itemSaleEndDate FROM Item WHERE itemNo = :itemNo")
	Optional<LocalDateTime> findItemSaleEndDateByItemNo(Integer itemNo);
	
	@Modifying
    @Query("UPDATE Item SET itemPrice = :price WHERE itemNo = :itemNo")
    int updateItemPrice(@Param("itemNo") Integer itemNo, @Param("price") BigDecimal newPrice);
}
