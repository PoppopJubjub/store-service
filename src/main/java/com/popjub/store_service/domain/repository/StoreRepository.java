package com.popjub.store_service.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.popjub.store_service.domain.entity.Store;

public interface StoreRepository{
	Store save(Store store);
	Optional<Store> findById(UUID storeId);
}
