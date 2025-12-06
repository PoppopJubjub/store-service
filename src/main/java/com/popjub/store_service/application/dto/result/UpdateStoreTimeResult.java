package com.popjub.store_service.application.dto.result;

import java.time.LocalTime;

import com.popjub.store_service.domain.entity.StoreTime;

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
