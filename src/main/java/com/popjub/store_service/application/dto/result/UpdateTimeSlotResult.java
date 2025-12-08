package com.popjub.store_service.application.dto.result;

import com.popjub.store_service.domain.entity.TimeSlot;
import com.popjub.store_service.domain.entity.TimeSlotStatus;

public record UpdateTimeSlotResult(
	TimeSlotStatus status
) {
	public static UpdateTimeSlotResult from(TimeSlot timeSlot) {
		return new UpdateTimeSlotResult(
			timeSlot.getStatus()
		);
	}
}
