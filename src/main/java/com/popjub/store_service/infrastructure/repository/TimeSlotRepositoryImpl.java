package com.popjub.store_service.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.popjub.store_service.domain.entity.TimeSlot;
import com.popjub.store_service.domain.repository.TimeslotRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class TimeSlotRepositoryImpl implements TimeslotRepository {
	private final TimeSlotJpaRepository timeSlotJpaRepository;

	@Override
	public List<TimeSlot> saveAll(List<TimeSlot> timeslots) {
		return timeSlotJpaRepository.saveAll(timeslots);
	}
}
