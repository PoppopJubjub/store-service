package com.popjub.store_service.presentation.dto.response;

import java.time.LocalTime;

import com.popjub.store_service.application.dto.result.SearchTimeRuleResult;

public record SearchTimeRuleResponse(
	String daysOfWeek,
	LocalTime startTime,
	LocalTime endTime
) {
	public static SearchTimeRuleResponse from(SearchTimeRuleResult result) {
		return new SearchTimeRuleResponse(

			result.daysOfWeek(),
			result.startTime(),
			result.endTime()
		);
	}
}
