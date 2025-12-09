package com.popjub.storeservice.application.dto.result;

import java.time.LocalTime;

public record SearchTimeRuleResult(
	String daysOfWeek,
	LocalTime startTime,
	LocalTime endTime
) {}
