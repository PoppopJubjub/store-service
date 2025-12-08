package com.popjub.store_service.presentation.dto.response;

import com.popjub.store_service.application.dto.result.UpdateTimeSlotResult;
import com.popjub.store_service.domain.entity.TimeSlotStatus;

public record UpdateTimeSlotResponse(
	TimeSlotStatus status
) {
	public static UpdateTimeSlotResponse from(UpdateTimeSlotResult result){
		return new UpdateTimeSlotResponse(
			result.status()
		);
	}
}
