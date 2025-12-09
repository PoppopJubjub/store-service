package com.popjub.storeservice.presentation.dto.response;

import java.time.LocalTime;

import com.popjub.storeservice.application.dto.result.UpdateStoreTimeResult;

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
