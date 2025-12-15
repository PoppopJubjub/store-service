package com.popjub.storeservice.application.scheduler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.popjub.storeservice.domain.repository.TimeSlotRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TimeSlotCloseScheduler {

	private final TimeSlotRepository timeSlotRepository;

	@Transactional
	@Scheduled(cron = "0 */10 8-22 * * *" , zone = "Asia/Seoul")
	// @Scheduled(fixedDelay = 10_000, initialDelay = 1_000) //테스트용
	public void closeTimeSlot() {
		LocalDateTime now = LocalDateTime.now();
		List<UUID> closedIds = timeSlotRepository.closedUpdate(now);

		log.info("[TimeSlotCloseScheduler] triggered at={}, closedCount={}", now, closedIds.size());
		if (!closedIds.isEmpty()) {
			log.debug("[TimeSlotCloseScheduler] closedIds(sample)={}", closedIds.stream().limit(5).toList());
		}
	}
}
