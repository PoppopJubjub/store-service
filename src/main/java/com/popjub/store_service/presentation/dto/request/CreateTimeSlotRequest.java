package com.popjub.store_service.presentation.dto.request;

import java.time.LocalDate;

import com.popjub.store_service.application.dto.command.CreateTimeSlotCommand;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateTimeSlotRequest(
	@NotNull(message = "날짜는 필수입니다.")
	LocalDate date,

	@NotNull(message = "interval은 필수입니다.")
	@Min(value = 1, message = "interval은 1분 이상이어야 합니다.")
	Integer intervalMinutes,

	@NotNull(message = "수용 인원(capacity)은 필수입니다.")
	@Min(value = 1, message = "수용 인원은 1명 이상이어야 합니다.")
	Integer capacity
) {

	public CreateTimeSlotCommand toCommand(){
		return new CreateTimeSlotCommand(
			date,
			intervalMinutes,
			capacity
		);
	}
}
