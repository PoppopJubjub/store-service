package com.popjub.store_service.application.validation;

import java.time.LocalTime;

import org.springframework.stereotype.Component;

import com.popjub.store_service.domain.entity.StoreTime;

@Component
public class TimeSlotValidator {

	public void validate(StoreTime storeTime) {
		validateWithinOperatingTime(storeTime);
	}

	private void validateWithinOperatingTime(StoreTime storeTime) {
		LocalTime open = storeTime.getStartTime();
		LocalTime close = storeTime.getEndTime();

		if (!open.isBefore(close)) {
			throw new IllegalArgumentException("운영 시작 시간이 종료 시간보다 같거나 늦을 수 없습니다.");
		}
	}
}
