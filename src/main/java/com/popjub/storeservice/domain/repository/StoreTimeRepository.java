package com.popjub.storeservice.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.popjub.storeservice.domain.entity.Store;
import com.popjub.storeservice.domain.entity.StoreTime;

public interface StoreTimeRepository{

	void saveAll(List<StoreTime> storeTimes);

	Optional<StoreTime> findByStoreAndDate(Store store, LocalDate date);

	List<StoreTime> findAllByStore(Store store);
}
