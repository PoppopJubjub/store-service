package com.popjub.store_service.domain.repository;

import java.time.LocalDate;
import java.util.Optional;

import com.popjub.store_service.domain.entity.Store;
import com.popjub.store_service.domain.entity.StoreTime;

public interface StoreTimeRepository{

	void saveAll(Iterable<StoreTime> storeTimes);

	Optional<StoreTime> findByStoreAndDate(Store store, LocalDate date);
}
