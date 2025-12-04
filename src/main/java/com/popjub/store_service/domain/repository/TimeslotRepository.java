package com.popjub.store_service.domain.repository;

import java.util.List;

import com.popjub.store_service.domain.entity.TimeSlot;

public interface TimeslotRepository {
	List<TimeSlot> saveAll(List<TimeSlot> timeslot);
}
