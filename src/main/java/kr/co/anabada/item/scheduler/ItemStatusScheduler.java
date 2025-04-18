package kr.co.anabada.item.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.anabada.item.entity.Item;
import kr.co.anabada.item.entity.Item.ItemStatus;
import kr.co.anabada.item.repository.BidRepository;
import kr.co.anabada.item.repository.ItemRepository;
import kr.co.anabada.user.entity.Buyer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ItemStatusScheduler {
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private BidRepository bidRepository;
	
	@Scheduled(cron = "0 * * * * *")
	@Transactional
	public void updateItemStatus() {
//		log.info("아이템 상태 업데이트 스케줄러 시작");

		try {
			int activatedCount = itemRepository.activateWaitingItems();
			if (activatedCount > 0) {
				log.info("{}개의 아이템이 ACTIVE 상태로 변경되었습니다.", activatedCount);
			}

			int expiredCount = itemRepository.expireActiveItems();
			if (expiredCount > 0) {
				log.info("{}개의 아이템이 EXPIRED 상태로 변경되었습니다.", expiredCount);
			}

			int updatedCount = 0;
			List<Item> itemsToReserve = itemRepository.findItemsToReserve();
			if (!itemsToReserve.isEmpty()) {
				for (Item item : itemsToReserve) {
					Buyer winner = bidRepository.findWinningBuyerByItem(item).orElse(null);
					
					if (winner != null) {
						item.setBuyer(winner);
						item.setItemStatus(ItemStatus.RESERVED);
						item.setItemResvStartDate(item.getItemSaleEndDate());
						item.setItemResvEndDate(item.getItemSaleEndDate().plusDays(3));
						itemRepository.save(item);
						updatedCount++;
					}
				}
				log.info("{}개의 아이템이 RESERVED 상태로 변경되고 예약 날짜가 설정되었습니다.", updatedCount);
			}

//			log.info("아이템 상태 업데이트 스케줄러 완료");

		} catch (Exception e) {
			log.error("아이템 상태 업데이트 실패: {}", e);
		}
	}
}
