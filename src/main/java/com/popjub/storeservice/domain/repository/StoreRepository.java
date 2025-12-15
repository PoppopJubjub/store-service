package com.popjub.storeservice.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.popjub.storeservice.domain.entity.Store;

public interface StoreRepository{
	boolean existsByStoreId(UUID storeId);
	Store save(Store store);
	Optional<Store> findById(UUID storeId);
	Page<Store> findAll(Pageable pageable);
}
