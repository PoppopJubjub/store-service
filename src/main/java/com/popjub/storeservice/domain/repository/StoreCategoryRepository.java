package com.popjub.storeservice.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.popjub.storeservice.domain.entity.Category;
import com.popjub.storeservice.domain.entity.Store;
import com.popjub.storeservice.domain.entity.StoreCategory;

public interface StoreCategoryRepository{
	void saveAll (List<StoreCategory> storeCategories);

	List<String> findCategoryNamesByStore(Store store);

	List<StoreCategory> findAllByCategory(Category category);

	Optional<StoreCategory> findByStoreAndCategory(Store store, Category category);

	List<StoreCategory> findAllByStore(Store store);

	List<String> findCategoryNamesByStoreId(UUID storeId);
}
