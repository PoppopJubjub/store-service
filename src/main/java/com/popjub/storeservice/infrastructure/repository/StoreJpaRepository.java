package com.popjub.storeservice.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.popjub.storeservice.domain.entity.Store;

public interface StoreJpaRepository extends JpaRepository<Store, UUID> {

	Optional<Store> findByStoreIdAndDeletedAtIsNull(UUID storeId);

	Page<Store> findAllByDeletedAtIsNull(Pageable pageable);
}
