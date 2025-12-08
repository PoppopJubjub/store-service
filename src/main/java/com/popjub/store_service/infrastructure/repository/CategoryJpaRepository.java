package com.popjub.store_service.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.popjub.store_service.domain.entity.Category;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

	boolean existsByCategoryName(String categoryName);
}
