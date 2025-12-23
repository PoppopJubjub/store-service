package com.popjub.storeservice.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.popjub.storeservice.domain.entity.Category;
import com.popjub.storeservice.domain.entity.Store;
import com.popjub.storeservice.domain.entity.StoreCategory;
import com.popjub.storeservice.domain.repository.StoreCategoryRepository;

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
		return storeCategoryJpaRepository.findActiveCategoryNamesByStore(store);
	}

	@Override
	public List<StoreCategory> findAllByCategory(Category category) {
		return storeCategoryJpaRepository.findAllByCategoryAndDeletedAtIsNull(category);
	}

	@Override
	public Optional<StoreCategory> findByStoreAndCategory(Store store, Category category) {
		return storeCategoryJpaRepository.findByStoreAndCategoryAndDeletedAtIsNull(store, category);
	}

	@Override
	public List<StoreCategory> findAllByStore(Store store) {
		return storeCategoryJpaRepository.findAllByStoreAndDeletedAtIsNull(store);
	}

	@Override
	public List<String> findCategoryNamesByStoreId(UUID storeId) {
		return storeCategoryJpaRepository.findCategoryNamesByStoreId(storeId);
	}
}
