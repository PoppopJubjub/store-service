package com.popjub.store_service.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.popjub.store_service.domain.entity.StoreCategory;

public interface StoreCategoryJpaRepository extends JpaRepository<StoreCategory,Long> {
}
