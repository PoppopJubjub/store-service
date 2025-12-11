package com.popjub.storeservice.application.dto.result;

import java.time.LocalDate;
import java.time.LocalTime;

import com.popjub.storeservice.domain.entity.TimeSlot;

public record GetRemainingResult(
	String storeName,
	LocalDate date,
	String status,
	LocalTime startTime,
	LocalTime endTime,
	Integer capacity,
	Integer remaining
) {
	public static GetRemainingResult from(TimeSlot timeSlot, Integer remaining){
		LocalTime endTime = timeSlot.getStartTime().plusMinutes(timeSlot.getInterval());
		return new GetRemainingResult(
			timeSlot.getStore().getName(),
			timeSlot.getDate(),
			timeSlot.getStatus().name(),
			timeSlot.getStartTime(),
			endTime,
			timeSlot.getCapacity(),
			remaining
		);
	}
}
