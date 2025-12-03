package com.popjub.store_service.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.popjub.store_service.domain.entity.StoreCategory;

@Repository
public interface StoreCategoryRepository extends JpaRepository<StoreCategory, Long> {
}
