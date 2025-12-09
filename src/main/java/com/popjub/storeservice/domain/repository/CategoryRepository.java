package com.popjub.storeservice.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.popjub.storeservice.domain.entity.Category;

public interface CategoryRepository {

	boolean existsByName(String categoryName);

	List<Category> findAllById(List<Long> categoryIds);

	Category save(Category category);

	Optional<Category> findById(Long categoryId);

	Page<Category> findAll(Pageable pageable);
}
