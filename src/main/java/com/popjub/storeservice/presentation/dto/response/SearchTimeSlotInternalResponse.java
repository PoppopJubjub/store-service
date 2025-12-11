package com.popjub.storeservice.presentation.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.popjub.storeservice.application.dto.result.SearchTimeSlotInternalResult;
import com.popjub.storeservice.domain.entity.TimeSlotStatus;

public record SearchTimeSlotInternalResponse(
	UUID timeslotId,
	UUID storeId,
	String storeName,
	LocalDate reservationDate,
	LocalTime reservationTime,
	TimeSlotStatus status
) {
	public static SearchTimeSlotInternalResponse from(SearchTimeSlotInternalResult result) {
		return new SearchTimeSlotInternalResponse(
			result.timeslotId(),
			result.storeId(),
			result.storeName(),
			result.reservationDate(),
			result.reservationTime(),
			result.status()
		);
	}
}