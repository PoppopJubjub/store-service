package com.popjub.storeservice.application.dto.result;

import java.time.LocalTime;

import com.popjub.storeservice.domain.entity.StoreTime;

public record UpdateStoreTimeResult(
	LocalTime startTime,
	LocalTime endTime
) {
	public static UpdateStoreTimeResult from(StoreTime storeTime) {
		return new UpdateStoreTimeResult(
			storeTime.getStartTime(),
			storeTime.getEndTime()
		);
	}
}
