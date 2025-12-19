package com.popjub.storeservice.application.scheduler;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.popjub.storeservice.application.service.StoreService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoreScheduler {

	private final StoreService storeService;

	@Transactional
	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
	/*@Scheduled(fixedDelay = 10_000, initialDelay = 1_000) //테스트용*/
	public void updateStoreStatus(){
		LocalDate today = LocalDate.now();
		storeService.openStores(today);
		storeService.closeStores(today);
	}
}
