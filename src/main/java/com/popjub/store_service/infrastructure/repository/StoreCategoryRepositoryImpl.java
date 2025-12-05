package com.popjub.store_service.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.popjub.store_service.domain.entity.Store;
import com.popjub.store_service.domain.entity.StoreCategory;
import com.popjub.store_service.domain.repository.StoreCategoryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StoreCategoryRepositoryImpl implements StoreCategoryRepository {
	private final StoreCategoryJpaRepository storeCategoryJpaRepository;

	@Override
	public void saveAll(List <StoreCategory> storeCategories) {
		 storeCategoryJpaRepository.saveAll(storeCategories);
	}

	@Override
	public List<String> findCategoryNamesByStore(Store store) {
		return storeCategoryJpaRepository.findByCategoryNameByStore(store);
	}
}
