package com.popjub.store_service.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.popjub.store_service.domain.entity.Category;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

	// 카테고리 이름 중복 체크
	boolean existsByCategoryName(String categoryName);

}
