package com.popjub.store_service.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.popjub.store_service.domain.entity.Category;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

	boolean existsByCategoryName(String categoryName);

	// ✅ 삭제되지 않은 카테고리만 페이지 조회
	Page<Category> findAllByDeletedAtIsNull(Pageable pageable);

	// ✅ 삭제되지 않은 단일 카테고리 조회
	Optional<Category> findByCategoryIdAndDeletedAtIsNull(Long categoryId);

	// ✅ 삭제되지 않은 카테고리들 (여러 개) 조회
	List<Category> findAllByCategoryIdInAndDeletedAtIsNull(List<Long> categoryIds);
}
