package com.popjub.store_service.infrastructure.repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.popjub.store_service.domain.entity.Store;
import com.popjub.store_service.domain.entity.StoreTime;

public interface StoreTimeJpaRepository extends JpaRepository<StoreTime, UUID> {

	Optional<StoreTime> findByStoreAndDate(Store store, LocalDate date);
}
