package com.popjub.storeservice.domain.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.popjub.storeservice.domain.entity.Store;
import com.popjub.storeservice.domain.entity.TimeSlot;

public interface TimeSlotRepository {
	List<TimeSlot> saveAll(List<TimeSlot> timeslot);

	Optional<TimeSlot> findById(UUID timeSlotId);

	Page<TimeSlot> findAll(Pageable pageable);

	Page<TimeSlot> findAllByStore_StoreIdAndDate(UUID storeId, LocalDate date, Pageable pageable);

	List<TimeSlot> findAllByStoreAndDate(Store store, LocalDate date);

	void deleteAllByStoreAndDate(Store store, LocalDate date);

	List<TimeSlot> findAllByStore(Store store);

	List<UUID> closedUpdate(LocalDateTime now);
}
