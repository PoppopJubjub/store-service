package com.popjub.storeservice.application.dto.command;

import java.time.LocalTime;

public record UpdateStoreTimeCommand(
	LocalTime startTime,
	LocalTime endTime
) {
}
