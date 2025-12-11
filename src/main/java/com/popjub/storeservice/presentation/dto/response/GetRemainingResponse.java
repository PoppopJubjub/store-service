package com.popjub.storeservice.presentation.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.popjub.storeservice.application.dto.result.GetRemainingResult;

public record GetRemainingResponse(
	String storeName,
	LocalDate date,
	String status,
	LocalTime startTime,
	LocalTime endTime,
	Integer capacity,
	Integer remaining
) {
	public static GetRemainingResponse from(GetRemainingResult result){
		return new GetRemainingResponse(
			result.storeName(),
			result.date(),
			result.status(),
			result.startTime(),
			result.endTime(),
			result.capacity(),
			result.remaining()
		);
	}
}
