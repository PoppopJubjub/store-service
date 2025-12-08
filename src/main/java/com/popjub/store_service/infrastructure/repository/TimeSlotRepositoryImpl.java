package com.popjub.store_service.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.popjub.store_service.domain.entity.Store;
import com.popjub.store_service.domain.entity.TimeSlot;
import com.popjub.store_service.domain.repository.TimeSlotRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class TimeSlotRepositoryImpl implements TimeSlotRepository {
	private final TimeSlotJpaRepository timeSlotJpaRepository;

	@Override
	public List<TimeSlot> saveAll(List<TimeSlot> timeslots) {
		return timeSlotJpaRepository.saveAll(timeslots);
	}

	@Override
	public Optional<TimeSlot> findById(UUID timeSlotId) {
		return timeSlotJpaRepository.findById(timeSlotId);
	}

	@Override
	public Page<TimeSlot> findAll(Pageable pageable) {
		return timeSlotJpaRepository.findAll(pageable);
	}

	@Override
	public Page<TimeSlot> findAllByStore_StoreIdAndDate(UUID storeId, LocalDate date, Pageable pageable) {
		return timeSlotJpaRepository.findAllByStore_StoreIdAndDateAndDeletedAtIsNull(storeId, date, pageable);
	}

	@Override
	public List<TimeSlot> findAllByStoreAndDate(Store store, LocalDate date) {
		return timeSlotJpaRepository.findAllByStoreAndDateAndDeletedAtIsNull(store, date);
	}

	@Override
	public void deleteAllByStoreAndDate(Store store, LocalDate date) {
		timeSlotJpaRepository.deleteAllByStoreAndDate(store, date);
	}

	@Override
	public List<TimeSlot> findAllByStore(Store store) {
		return timeSlotJpaRepository.findAllByStoreAndDeletedAtIsNull(store);
	}
}
