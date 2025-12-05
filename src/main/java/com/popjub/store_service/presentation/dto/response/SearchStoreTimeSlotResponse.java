package com.popjub.store_service.presentation.dto.response;

import java.util.List;

import com.popjub.store_service.application.dto.result.SearchTimeSlotResult;

public record SearchStoreTimeSlotResponse(
	String storeName,
	List<TimeSlotSummaryResponse> timeSlots
) {
	public static SearchStoreTimeSlotResponse from(String storeName, List<SearchTimeSlotResult> results) {
		List<TimeSlotSummaryResponse> slots = results.stream()
			.map(TimeSlotSummaryResponse::from)
			.toList();

		return new SearchStoreTimeSlotResponse(storeName, slots);
	}
}
