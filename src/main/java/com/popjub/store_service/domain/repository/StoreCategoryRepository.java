package com.popjub.store_service.domain.repository;

import java.util.List;

import com.popjub.store_service.domain.entity.Store;
import com.popjub.store_service.domain.entity.StoreCategory;

public interface StoreCategoryRepository{
	void saveAll (List<StoreCategory> storeCategories);

	List<String> findCategoryNamesByStore(Store store);
}
