package com.popjub.store_service.presentation.dto.response;

import java.util.List;
import java.util.UUID;

import com.popjub.store_service.application.dto.result.CreateTimeSlotResult;

public record CreateTimeSlotResponse(
	List<UUID> TimeslotId
) {
	public static CreateTimeSlotResponse from(CreateTimeSlotResult result) {
		return new CreateTimeSlotResponse(result.timeSlotId());
	}

}
