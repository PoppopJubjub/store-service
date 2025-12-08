package com.popjub.store_service.presentation.dto.request;

import java.time.LocalTime;

import com.popjub.store_service.application.dto.command.UpdateStoreTimeCommand;

import jakarta.validation.constraints.NotNull;

public record UpdateStoreTimeRequest(

	@NotNull(message = "영업 시작 시간은 필수입니다.")
	LocalTime startTime,

	@NotNull(message = "영업 종료 시간은 필수입니다.")
	LocalTime endTime
) {
	public UpdateStoreTimeCommand toCommand() {
		return new UpdateStoreTimeCommand(
			startTime,
			endTime
		);
	}
}
