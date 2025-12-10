package com.popjub.storeservice.application.validation;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.popjub.storeservice.domain.entity.Store;
import com.popjub.storeservice.domain.entity.StoreTime;
import com.popjub.storeservice.domain.entity.TimeSlot;
import com.popjub.storeservice.exception.StoreCustomException;
import com.popjub.storeservice.exception.StoreErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TimeSlotValidator {

	private final StoreValidator storeValidator;

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

	public void validateUpdateTimeSlot(TimeSlot timeSlot, Long currentUserId, List<String> role){
		Store store = timeSlot.getStore();
		storeValidator.validateManagerOrAdmin(store, currentUserId, role);
	}
}
