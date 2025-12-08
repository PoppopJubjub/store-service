package com.popjub.store_service.presentation.dto.response;

import java.time.LocalTime;

import com.popjub.store_service.application.dto.result.UpdateStoreTimeResult;

public record UpdateStoreTimeResponse(
	LocalTime startTime,
	LocalTime endTime
) {
	public static UpdateStoreTimeResponse from(UpdateStoreTimeResult result) {
		return new UpdateStoreTimeResponse(
			result.startTime(),
			result.endTime()
		);
	}
}
