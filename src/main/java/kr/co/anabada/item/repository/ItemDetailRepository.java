package kr.co.anabada.item.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.co.anabada.item.dto.ItemTimeInfoDTO;
import kr.co.anabada.item.entity.Bid.BidStatus;
import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.entity.Item.ItemStatus;

@Repository
public interface ItemDetailRepository extends JpaRepository<Item, Integer> {
	@Query("SELECT itemPrice FROM Item WHERE itemNo = :itemNo")
	Optional<BigDecimal> findItemPriceByItemNo(@Param("itemNo") Integer itemNo);
	
	@Query("SELECT new kr.co.anabada.item.dto.ItemTimeInfoDTO"
			+ "(i.itemSaleStartDate, i.itemSaleEndDate, i.itemStatus) "
			+ "FROM Item i "
			+ "WHERE i.itemNo = :itemNo")
	Optional<ItemTimeInfoDTO> findTimeInfoByItemNo(@Param("itemNo") Integer itemNo);

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

	@Query("SELECT DISTINCT i "
			+ "FROM Item i "
			+ "WHERE i.seller.user.userNo = :userNo "
			+ "AND (:status IS NULL OR i.itemStatus = :status)")
	Page<Item> findBySellerUserNoAndOptionalItemStatus(
			@Param("userNo") Integer userNo, @Param("status") ItemStatus status, Pageable pageable);
	
	@Query("SELECT DISTINCT i "
			+ "FROM Item i "
			+ "JOIN Bid b ON b.item = i "
			+ "JOIN b.buyer buyer "
			+ "WHERE buyer.user.userNo = :userNo "
			+ "AND (:status IS NULL OR i.itemStatus = :status)")
	Page<Item> findByBuyerUserNoAndOptionalItemStatus(
			@Param("userNo") Integer userNo, @Param("status") ItemStatus status, Pageable pageable);
	
	// UserProfileScheduler : itemCount, activeItemCount (updateDailyStatistics)
	@Query("SELECT i.seller.sellerNo, COUNT(i.itemNo) "
			+ "FROM Item i "
			+ "WHERE (:status IS NULL OR i.itemStatus = :status) "
			+ "GROUP BY i.seller.sellerNo")
	List<Object[]> countItemsPerSellerByOptionalItemStatus(@Param("status") ItemStatus status);
	
	// UserProfileScheduler : activeBidItemCount (updateDailyStatistics)
	@Query("SELECT b.buyer.buyerNo, COUNT(DISTINCT b.item.itemNo) "
			+ "FROM Bid b JOIN b.item i "
			+ "WHERE i.itemStatus = :itemStatus "
			+ "AND (:bidStatus IS NULL OR b.bidStatus = :bidStatus) "
			+ "GROUP BY b.buyer.buyerNo")
	List<Object[]> countItemsPerBuyerByItemStatusAndOptionalBidStatus(
			@Param("itemStatus") ItemStatus itemStatus, @Param("bidStatus") BidStatus bidStatus);

	@Query("SELECT b.buyer.buyerNo, COUNT(DISTINCT b.item.itemNo) "
			+ "FROM Bid b JOIN b.item i "
			+ "GROUP BY b.buyer.buyerNo")
	List<Object[]> countBidItemsPerBuyer();
}
