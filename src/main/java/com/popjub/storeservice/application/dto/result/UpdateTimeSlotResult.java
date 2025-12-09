package com.popjub.storeservice.application.dto.result;

import com.popjub.storeservice.domain.entity.TimeSlot;
import com.popjub.storeservice.domain.entity.TimeSlotStatus;

public record UpdateTimeSlotResult(
	TimeSlotStatus status
) {
	public static UpdateTimeSlotResult from(TimeSlot timeSlot) {
		return new UpdateTimeSlotResult(
			timeSlot.getStatus()
		);
	}
}
