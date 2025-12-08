package com.popjub.store_service.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.popjub.store_service.domain.entity.Category;
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

	@Override
	public List<StoreCategory> findAllByCategory(Category category) {
		return storeCategoryJpaRepository.findAllByCategory(category);
	}

	@Override
	public Optional<StoreCategory> findByStoreAndCategory(Store store, Category category) {
		return storeCategoryJpaRepository.findByStoreAndCategory(store, category);
	}
}
