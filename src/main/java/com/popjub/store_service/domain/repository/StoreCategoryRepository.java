package com.popjub.store_service.domain.repository;

import com.popjub.store_service.domain.entity.StoreCategory;

public interface StoreCategoryRepository{
	void saveAll (Iterable<StoreCategory> storeCategories);
}
