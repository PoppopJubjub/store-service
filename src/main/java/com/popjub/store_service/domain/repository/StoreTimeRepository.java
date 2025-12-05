package com.popjub.store_service.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.popjub.store_service.domain.entity.Store;
import com.popjub.store_service.domain.entity.StoreTime;

public interface StoreTimeRepository{

	void saveAll(List<StoreTime> storeTimes);

	Optional<StoreTime> findByStoreAndDate(Store store, LocalDate date);

	List<StoreTime> findAllByStore(Store store);
}
