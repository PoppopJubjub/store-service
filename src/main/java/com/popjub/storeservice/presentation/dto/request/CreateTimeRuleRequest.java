package com.popjub.storeservice.presentation.dto.request;

import java.time.LocalTime;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateTimeRuleRequest(

	@NotEmpty(message = "요일 목록은 비어 있을 수 없습니다.")
	List<
		@Pattern(
			regexp = "MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY",
			message = "요일은 MONDAY~SUNDAY 형식이어야 합니다."
		)
			String
		> daysOfWeek,

	@NotNull(message = "시작 시간은 필수입니다.")
	LocalTime startTime,

	@NotNull(message = "종료 시간은 필수입니다.")
	LocalTime endTime

) {}


