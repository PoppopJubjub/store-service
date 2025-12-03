package com.popjub.store_service.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.popjub.store_service.domain.entity.Category;
import com.popjub.store_service.domain.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class CategoryRepositoryImpl implements CategoryRepository {
	private final CategoryJpaRepository categoryJpaRepository;

	@Override
	public boolean existsByName(String name) {
		return categoryJpaRepository.existsByName(name);
	}

	@Override
	public List<Category> findAllById(List<Long> categoryIds) {
		return categoryJpaRepository.findAllById(categoryIds);
	}

	@Override
	public Category save(Category category) {
		return categoryJpaRepository.save(category);
	}
}
