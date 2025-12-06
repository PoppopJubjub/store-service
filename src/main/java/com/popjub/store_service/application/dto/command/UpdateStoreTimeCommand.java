package com.popjub.store_service.application.dto.command;

import java.time.LocalTime;

public record UpdateStoreTimeCommand(
	LocalTime startTime,
	LocalTime endTime
) {
}
