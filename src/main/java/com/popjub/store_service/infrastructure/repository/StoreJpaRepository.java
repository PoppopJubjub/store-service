package com.popjub.store_service.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.popjub.store_service.domain.entity.Store;

public interface StoreJpaRepository extends JpaRepository<Store, UUID> {

	Optional<Store> findByStoreIdAndDeletedAtIsNull(UUID storeId);

	Page<Store> findAllByDeletedAtIsNull(Pageable pageable);
}
