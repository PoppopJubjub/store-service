package com.popjub.store_service.domain.repository;

import java.util.List;
import java.util.Optional;

import com.popjub.store_service.domain.entity.Category;
import com.popjub.store_service.domain.entity.Store;
import com.popjub.store_service.domain.entity.StoreCategory;

public interface StoreCategoryRepository{
	void saveAll (List<StoreCategory> storeCategories);

	List<String> findCategoryNamesByStore(Store store);

	List<StoreCategory> findAllByCategory(Category category);

	Optional<StoreCategory> findByStoreAndCategory(Store store, Category category);
}
