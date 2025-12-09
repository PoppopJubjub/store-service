package com.popjub.storeservice.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.popjub.storeservice.domain.entity.Category;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

	boolean existsByCategoryName(String categoryName);

	Page<Category> findAllByDeletedAtIsNull(Pageable pageable);

	Optional<Category> findByCategoryIdAndDeletedAtIsNull(Long categoryId);

	List<Category> findAllByCategoryIdInAndDeletedAtIsNull(List<Long> categoryIds);
}
