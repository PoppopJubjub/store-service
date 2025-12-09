package com.popjub.storeservice.application.dto.result;

import java.util.List;
import java.util.UUID;

import com.popjub.storeservice.domain.entity.TimeSlot;

public record CreateTimeSlotResult(
	List<UUID> timeSlotId
) {
	public static CreateTimeSlotResult from(List<TimeSlot> timeslots) {
		List<UUID> timeSlots = timeslots.stream()
			.map(TimeSlot::getTimeslotId)
			.toList();
		return new CreateTimeSlotResult(timeSlots);
	}
}
