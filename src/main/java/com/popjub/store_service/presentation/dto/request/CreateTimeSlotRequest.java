package com.popjub.store_service.presentation.dto.request;

import java.time.LocalDate;

import com.popjub.store_service.application.dto.command.CreateTimeSlotCommand;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateTimeSlotRequest(
	@NotNull(message = "날짜는 필수입니다.")
	LocalDate date,

	@NotBlank(message = "인터벌은 필수값입니다.")
	@Pattern(regexp = "30|60", message = "인터벌은 30 또는 60만 가능합니다.")
	String intervalMinutes,

	@NotNull(message = "수용 인원(capacity)은 필수입니다.")
	@Min(value = 1, message = "수용 인원은 1명 이상이어야 합니다.")
	@Max(value = 999, message = "수용 인원은 최대 999명입니다.")
	Integer capacity
) {

	public CreateTimeSlotCommand toCommand(){
		int interval = Integer.parseInt(intervalMinutes);
		return new CreateTimeSlotCommand(
			date,
			interval,
			capacity
		);
	}
}
