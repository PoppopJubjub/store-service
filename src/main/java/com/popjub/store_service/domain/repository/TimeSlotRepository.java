package com.popjub.store_service.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.popjub.store_service.domain.entity.Store;
import com.popjub.store_service.domain.entity.TimeSlot;

public interface TimeSlotRepository {
	List<TimeSlot> saveAll(List<TimeSlot> timeslot);

	Optional<TimeSlot> findById(UUID timeSlotId);

	Page<TimeSlot> findAll(Pageable pageable);

	Page<TimeSlot> findAllByStore_StoreIdAndDate(UUID storeId, LocalDate date, Pageable pageable);

	List<TimeSlot> findAllByStoreAndDate(Store store, LocalDate date);

	void deleteAllByStoreAndDate(Store store, LocalDate date);
}
