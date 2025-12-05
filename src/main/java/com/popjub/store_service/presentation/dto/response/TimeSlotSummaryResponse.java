package com.popjub.store_service.presentation.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.popjub.store_service.application.dto.result.SearchTimeSlotResult;

public record TimeSlotSummaryResponse(
	LocalDate date,
	String status,
	LocalTime startTime,
	LocalTime endTime,
	Integer capacity
) {
	public static TimeSlotSummaryResponse from(SearchTimeSlotResult result) {
		return new TimeSlotSummaryResponse(
			result.date(),
			result.status(),
			result.startTime(),
			result.endTime(),
			result.capacity()
		);
	}
}
