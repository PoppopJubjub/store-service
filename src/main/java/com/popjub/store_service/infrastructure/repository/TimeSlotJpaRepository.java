package com.popjub.store_service.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.popjub.store_service.domain.entity.Store;
import com.popjub.store_service.domain.entity.TimeSlot;

public interface TimeSlotJpaRepository extends JpaRepository<TimeSlot, UUID> {
	Page<TimeSlot> findAllByStore_StoreIdAndDateAndDeletedAtIsNull(UUID storeId, LocalDate date, Pageable pageable);

	List<TimeSlot> findAllByStoreAndDateAndDeletedAtIsNull(Store store, LocalDate date);

	void deleteAllByStoreAndDate(Store store, LocalDate date);

	List<TimeSlot> findAllByStoreAndDeletedAtIsNull(Store store);
}
