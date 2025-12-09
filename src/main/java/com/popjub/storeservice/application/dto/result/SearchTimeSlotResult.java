package com.popjub.storeservice.application.dto.result;

import java.time.LocalDate;
import java.time.LocalTime;

import com.popjub.storeservice.domain.entity.TimeSlot;

public record SearchTimeSlotResult (
	String storeName,
	LocalDate date,
	String status,
	LocalTime startTime,
	LocalTime endTime,
	Integer capacity
){
	public static SearchTimeSlotResult from(TimeSlot timeSlot){
		//interval로 endTime 계산
		LocalTime endTime = timeSlot.getStartTime().plusMinutes(timeSlot.getInterval());

		return new SearchTimeSlotResult(
			timeSlot.getStore().getName(),
			timeSlot.getDate(),
			timeSlot.getStatus().name(),
			timeSlot.getStartTime(),
			endTime,
			timeSlot.getCapacity()
		);
	}
}
