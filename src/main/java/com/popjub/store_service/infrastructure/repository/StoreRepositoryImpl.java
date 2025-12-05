package com.popjub.store_service.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.popjub.store_service.domain.entity.Store;
import com.popjub.store_service.domain.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepository {
	private final StoreJpaRepository storeJpaRepository;

	@Override
	public Store save(Store store) {
		return storeJpaRepository.save(store);
	}

	@Override
	public Optional<Store> findById(UUID storeId) {
		return storeJpaRepository.findById(storeId);
	}

	@Override
	public Page<Store> findAll(Pageable pageable) {
		return storeJpaRepository.findAll(pageable);
	}
}
