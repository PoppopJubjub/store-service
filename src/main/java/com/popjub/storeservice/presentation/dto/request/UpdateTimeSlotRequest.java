package com.popjub.storeservice.presentation.dto.request;

import com.popjub.storeservice.application.dto.command.UpdateTimeSlotCommand;
import com.popjub.storeservice.domain.entity.TimeSlotStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateTimeSlotRequest(

	@NotNull(message = "status값은 Null일 수 없습니다.")
	TimeSlotStatus status
) {
	public UpdateTimeSlotCommand toCommand(){
		return new UpdateTimeSlotCommand(
			status
		);
	}
}
