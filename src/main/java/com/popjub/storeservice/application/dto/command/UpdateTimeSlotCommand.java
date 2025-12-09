package com.popjub.storeservice.application.dto.command;

import com.popjub.storeservice.domain.entity.TimeSlotStatus;

public record UpdateTimeSlotCommand(
	TimeSlotStatus status
) { }
