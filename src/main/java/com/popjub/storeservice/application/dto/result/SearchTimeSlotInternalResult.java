package com.popjub.storeservice.application.dto.result;// com.popjub.storeservice.application.dto.result

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.popjub.storeservice.domain.entity.TimeSlot;
import com.popjub.storeservice.domain.entity.TimeSlotStatus;

public record SearchTimeSlotInternalResult(
	UUID timeslotId,
	UUID storeId,
	String storeName,
	LocalDate reservationDate,
	LocalTime reservationTime,
	TimeSlotStatus status
) {
	public static SearchTimeSlotInternalResult from(TimeSlot timeslot) {
		return new SearchTimeSlotInternalResult(
			timeslot.getTimeslotId(),
			timeslot.getStore().getStoreId(),
			timeslot.getStore().getName(),
			timeslot.getDate(),
			timeslot.getStartTime(),
			TimeSlotStatus.valueOf(timeslot.getStatus().name())
		);
	}
}