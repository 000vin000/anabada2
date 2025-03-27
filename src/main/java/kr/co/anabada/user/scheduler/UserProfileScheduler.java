package kr.co.anabada.user.scheduler;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import kr.co.anabada.buy.repository.PaymentRepository;
import kr.co.anabada.item.repository.ItemRepository;
import kr.co.anabada.user.entity.Buyer;
import kr.co.anabada.user.entity.Seller;
import kr.co.anabada.user.entity.Seller.SellerGrade;
import kr.co.anabada.user.repository.BuyerRepository;
import kr.co.anabada.user.repository.SellerRepository;

@Component
public class UserProfileScheduler {
	@Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private PaymentRepository paymentRepository;
//    @Autowired
//    private ReviewRepository reviewRepository;
	@Autowired
	private BuyerRepository buyerRepository;
    
	@Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void updateSellerStatistics() {
        List<Integer> sellerNos = sellerRepository.findAllSellerNos();
        
        for (Integer sellerNo : sellerNos) {
        	try {
                processSellerStatistics(sellerNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	
	private void processSellerStatistics(Integer sellerNo) {
		Seller seller = sellerRepository.findById(sellerNo)
		        .orElseThrow(() -> new EntityNotFoundException("판매자 프로필이 존재하지 않습니다: " + sellerNo));

    	int itemCount = calculateItemCount(sellerNo);
    	int transCount = calculateTransCount(sellerNo);
        BigDecimal totalSales = calculateTotalSales(sellerNo);
        double averageRating = calculateAverageRating(sellerNo);
        //TODO Seller 계산 필드 추가 재고
		
        seller.setSellerItemCnt(itemCount);
        seller.setSellerTransCnt(transCount);
        seller.setSellerTotalSales(totalSales);
        seller.setSellerAvgRating(averageRating);
        
        sellerRepository.save(seller);
	}
	
    @Scheduled(cron = "0 30 2 * * *")
    @Transactional
    public void updateBuyerStatistics() {
        List<Integer> buyerNos = buyerRepository.findAllBuyerNos();
        
        for (Integer buyerNo : buyerNos) {
        	try {
                processBuyerStatistics(buyerNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void processBuyerStatistics(Integer buyerNo) {
		Buyer buyer = buyerRepository.findById(buyerNo)
		        .orElseThrow(() -> new EntityNotFoundException("구매자 프로필이 존재하지 않습니다: " + buyerNo));
    	
    	int bidCount = calculateBidCount(buyer);
        int successBidCount = calculateSuccessBidCount(buyer);
        //TODO Buyer 계산 필드 추가 재고
        
        buyer.setBuyerBidCnt(bidCount);
        buyer.setBuyerSuccessBidCnt(successBidCount);
        
        buyerRepository.save(buyer);
	}
    

	@Scheduled(cron = "0 0 0 1 * *")
	@Transactional
	public void updateSellerGrades() {
	    List<Integer> sellerNos = sellerRepository.findAllSellerNos();
	    
        for (Integer sellerNo : sellerNos) {
        	try {
        		processSellerGrade(sellerNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
	
	private void processSellerGrade(Integer sellerNo) {
		if (!sellerRepository.existsById(sellerNo)) {
			throw new EntityNotFoundException("판매자 프로필이 존재하지 않습니다: " + sellerNo);
		}
		
//	    LocalDateTime startDate = YearMonth.now().minusMonths(1).atDay(1).atStartOfDay();
//	    LocalDateTime endDate = YearMonth.now().atDay(1).atStartOfDay();
	    LocalDateTime startDate = YearMonth.now().atDay(1).atStartOfDay(); //테스트 or 하루마다 스케줄링할 경우
	    LocalDateTime endDate = LocalDateTime.now();
		int itemCount = paymentRepository.countBySellerNoAndDateRange(sellerNo, startDate, endDate);
		
		SellerGrade newSellerGrade = SellerGrade.fromSalesCount(itemCount);
        sellerRepository.updateSellerGrade(sellerNo, newSellerGrade);
	}
	
	private int calculateBidCount(Buyer buyer) {
		//TODO processBuyerStatistics()
		return 0;
	}
	
	private int calculateSuccessBidCount(Buyer buyer) {
		//TODO processBuyerStatistics()
		return 0;
	}

	private int calculateItemCount(Integer sellerNo) {
		return itemRepository.countBySeller_SellerNo(sellerNo);
	}

	private int calculateTransCount(Integer sellerNo) {
		return paymentRepository.countBySellerNo(sellerNo);
    }
	
	private BigDecimal calculateTotalSales(Integer sellerNo) {
		return paymentRepository.sumSalesBySellerNo(sellerNo);
    }
	
	private double calculateAverageRating(Integer sellerNo) {
		//TODO 리뷰쪽 구조 확정되면 작성
		return 0;
    }
}
