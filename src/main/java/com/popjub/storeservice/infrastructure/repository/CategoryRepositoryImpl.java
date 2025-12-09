package com.popjub.storeservice.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.popjub.storeservice.domain.entity.Category;
import com.popjub.storeservice.domain.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

	private final CategoryJpaRepository categoryJpaRepository;

	@Override
	public boolean existsByName(String categoryName) {
		return categoryJpaRepository.existsByCategoryName(categoryName);
	}

	@Override
	public List<Category> findAllById(List<Long> categoryIds) {
		return categoryJpaRepository.findAllByCategoryIdInAndDeletedAtIsNull(categoryIds);
	}

	@Override
	public Category save(Category category) {
		return categoryJpaRepository.save(category);
	}

	@Override
	public Optional<Category> findById(Long categoryId) {
		return categoryJpaRepository.findByCategoryIdAndDeletedAtIsNull(categoryId);
	}

	@Override
	public Page<Category> findAll(Pageable pageable) {
		return categoryJpaRepository.findAllByDeletedAtIsNull(pageable);
	}
}
