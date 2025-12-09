package com.popjub.storeservice.presentation.dto.response;

import com.popjub.storeservice.application.dto.result.UpdateTimeSlotResult;
import com.popjub.storeservice.domain.entity.TimeSlotStatus;

public record UpdateTimeSlotResponse(
	TimeSlotStatus status
) {
	public static UpdateTimeSlotResponse from(UpdateTimeSlotResult result){
		return new UpdateTimeSlotResponse(
			result.status()
		);
	}
}
