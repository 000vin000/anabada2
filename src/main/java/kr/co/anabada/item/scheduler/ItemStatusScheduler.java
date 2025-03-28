package kr.co.anabada.item.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.anabada.item.repository.ItemRepository;

@Component
public class ItemStatusScheduler {
	@Autowired
	ItemRepository itemRepository;

	@Scheduled(cron = "0 * * * * *")
	@Transactional
	public void updateItemStatus() {
		itemRepository.activateWaitingItems();
		itemRepository.expireActiveItems();
		itemRepository.reserveActiveItems();
	}
}
