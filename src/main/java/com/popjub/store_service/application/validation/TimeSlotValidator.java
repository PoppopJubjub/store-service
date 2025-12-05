package com.popjub.store_service.application.validation;

import java.time.LocalTime;

import org.springframework.stereotype.Component;

import com.popjub.store_service.domain.entity.StoreTime;
import com.popjub.store_service.exception.StoreCustomException;
import com.popjub.store_service.exception.StoreErrorCode;

@Component
public class TimeSlotValidator {

	public void validate(StoreTime storeTime) {
		validateWithinOperatingTime(storeTime);
	}

	private void validateWithinOperatingTime(StoreTime storeTime) {
		LocalTime open = storeTime.getStartTime();
		LocalTime close = storeTime.getEndTime();

		if (!open.isBefore(close)) {
			throw new StoreCustomException(StoreErrorCode.INVALID_STORE_TIME_RANGE);
		}
	}
}
