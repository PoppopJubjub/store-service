package com.popjub.storeservice.application.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.popjub.storeservice.application.service.TimeSlotService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TimeSlotCloseScheduler {

	private final TimeSlotService timeSlotService;

	@Transactional
	@Scheduled(cron = "0 */10 8-22 * * *" , zone = "Asia/Seoul")
	/*@Scheduled(fixedDelay = 10_000, initialDelay = 1_000) //테스트용*/
	public void closeTimeSlot() {
		timeSlotService.closeAndSend(LocalDateTime.now());
	}
}

