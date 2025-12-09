package com.popjub.storeservice.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.popjub.storeservice.domain.entity.Store;
import com.popjub.storeservice.domain.entity.StoreTime;

public interface StoreTimeJpaRepository extends JpaRepository<StoreTime, UUID> {

	Optional<StoreTime> findByStoreAndDate(Store store, LocalDate date);
	List<StoreTime> findAllByStore(Store store);
}
