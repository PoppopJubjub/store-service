package com.popjub.store_service.presentation.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.popjub.store_service.application.dto.result.SearchTimeSlotResult;

public record SearchTimeSlotResponse(
	String storeName,
	LocalDate date,
	String status,
	LocalTime startTime,
	LocalTime endTime,
	Integer capacity
) {
	public static SearchTimeSlotResponse from(SearchTimeSlotResult result) {
		return new  SearchTimeSlotResponse(
			result.storeName(),
			result.date(),
			result.status(),
			result.startTime(),
			result.endTime(),
			result.capacity()
		);
	}
}
