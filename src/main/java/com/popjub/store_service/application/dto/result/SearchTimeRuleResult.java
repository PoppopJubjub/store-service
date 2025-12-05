package com.popjub.store_service.application.dto.result;

import java.time.LocalTime;

public record SearchTimeRuleResult(
	String daysOfWeek,
	LocalTime startTime,
	LocalTime endTime
) {}
