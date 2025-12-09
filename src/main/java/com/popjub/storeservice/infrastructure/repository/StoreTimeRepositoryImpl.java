package com.popjub.storeservice.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.popjub.storeservice.domain.entity.Store;
import com.popjub.storeservice.domain.entity.StoreTime;
import com.popjub.storeservice.domain.repository.StoreTimeRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StoreTimeRepositoryImpl implements StoreTimeRepository {
	private final StoreTimeJpaRepository storeTimeJpaRepository;



	@Override
	public Optional<StoreTime> findByStoreAndDate(Store store, LocalDate date) {
		return storeTimeJpaRepository.findByStoreAndDate(store, date);
	}

	@Override
	public List<StoreTime> findAllByStore(Store store) {
		return storeTimeJpaRepository.findAllByStore(store);
	}

	@Override
	public void saveAll(List<StoreTime> storeTimes) {
		storeTimeJpaRepository.saveAll(storeTimes);
	}
}
