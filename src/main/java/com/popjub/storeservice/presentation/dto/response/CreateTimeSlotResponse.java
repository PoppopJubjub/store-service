package com.popjub.storeservice.presentation.dto.response;

import java.util.List;
import java.util.UUID;

import com.popjub.storeservice.application.dto.result.CreateTimeSlotResult;

public record CreateTimeSlotResponse(
	List<UUID> TimeslotId
) {
	public static CreateTimeSlotResponse from(CreateTimeSlotResult result) {
		return new CreateTimeSlotResponse(result.timeSlotId());
	}

}
