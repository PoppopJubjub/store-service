package com.popjub.store_service.application.dto.command;

import com.popjub.store_service.domain.entity.TimeSlotStatus;

public record UpdateTimeSlotCommand(
	TimeSlotStatus status
) { }
