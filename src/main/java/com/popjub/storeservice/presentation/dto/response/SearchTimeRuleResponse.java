package com.popjub.storeservice.presentation.dto.response;

import java.time.LocalTime;

import com.popjub.storeservice.application.dto.result.SearchTimeRuleResult;

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
