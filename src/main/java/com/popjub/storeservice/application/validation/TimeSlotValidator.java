package com.popjub.storeservice.application.validation;

import java.time.LocalTime;

import org.springframework.stereotype.Component;

import com.popjub.storeservice.domain.entity.StoreTime;
import com.popjub.storeservice.exception.StoreCustomException;
import com.popjub.storeservice.exception.StoreErrorCode;

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
