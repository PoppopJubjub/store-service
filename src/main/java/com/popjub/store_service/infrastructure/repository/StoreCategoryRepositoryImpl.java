package com.popjub.store_service.infrastructure.repository;

import org.springframework.stereotype.Repository;

import com.popjub.store_service.domain.entity.StoreCategory;
import com.popjub.store_service.domain.repository.StoreCategoryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StoreCategoryRepositoryImpl implements StoreCategoryRepository {
	private final StoreCategoryJpaRepository storeCategoryJpaRepository;

	@Override
	public void saveAll(Iterable<StoreCategory> storeCategories) {
		 storeCategoryJpaRepository.saveAll(storeCategories);
	}
}
