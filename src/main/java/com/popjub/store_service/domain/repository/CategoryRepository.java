package com.popjub.store_service.domain.repository;

import java.util.List;

import com.popjub.store_service.domain.entity.Category;

public interface CategoryRepository{
	boolean existsByName(String name);
	List<Category> findAllById(List<Long> categoryIds);
	Category save(Category category);
}

