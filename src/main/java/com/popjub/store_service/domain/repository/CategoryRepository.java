package com.popjub.store_service.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.popjub.store_service.domain.entity.Category;

public interface CategoryRepository {

	boolean existsByName(String categoryName);

	List<Category> findAllById(List<Long> categoryIds);

	Category save(Category category);

	Optional<Category> findById(Long categoryId);

	Page<Category> findAll(Pageable pageable);
}
