package kr.co.anabada.item.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.entity.Item.ItemStatus;

@Repository
public interface ItemDetailRepository extends JpaRepository<Item, Integer> {
	@Query("SELECT itemPrice FROM Item WHERE itemNo = :itemNo")
	Optional<BigDecimal> findItemPriceByItemNo(@Param("itemNo") Integer itemNo);

	@Query("SELECT itemStatus FROM Item WHERE itemNo = :itemNo")
	Optional<ItemStatus> findItemStatusByItemNo(@Param("itemNo") Integer itemNo);

	@Query("SELECT itemSaleStartDate FROM Item WHERE itemNo = :itemNo")
	Optional<LocalDateTime> findItemSaleStartDateByItemNo(@Param("itemNo") Integer itemNo);

	@Query("SELECT itemSaleEndDate FROM Item WHERE itemNo = :itemNo")
	Optional<LocalDateTime> findItemSaleEndDateByItemNo(@Param("itemNo") Integer itemNo);

	@Modifying
	@Query("UPDATE Item SET itemPrice = :newPrice WHERE itemNo = :itemNo")
	int updateItemPrice(@Param("itemNo") Integer itemNo, @Param("newPrice") BigDecimal newPrice);

	@Modifying
	@Query("UPDATE Item SET itemViewCnt = itemViewCnt + 1 "
			+ "WHERE itemNo = :itemNo AND (:userNo IS NULL OR seller.sellerNo != :userNo)")
	void incrementItemViewCount(@Param("itemNo") Integer itemNo, @Param("userNo") Integer userNo);

	Page<Item> findBySellerUserUserNo(@Param("userNo") Integer userNo, Pageable pageable);

	Page<Item> findBySellerUserUserNoAndItemStatus(
			@Param("userNo") Integer userNo, @Param("status") ItemStatus status, Pageable pageable);
	
	@Query("SELECT i FROM Item i "
			+ "JOIN Bid b ON b.item = i "
			+ "JOIN User u ON b.user = u "
			+ "JOIN Buyer by ON by.user = u "
			+ "WHERE by.user.userNo = :userNo "
			+ "AND (:status IS NULL OR i.itemStatus = :status)")
	Page<Item> findByBuyerNoAndOptionalItemStatus(
			@Param("userNo") Integer userNo, @Param("status") ItemStatus status, Pageable pageable);
}
