package com.popjub.store_service.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.popjub.store_service.domain.entity.Category;
import com.popjub.store_service.domain.repository.CategoryRepository;
import com.popjub.store_service.presentation.dto.request.CreateCategoryRequest;
import com.popjub.store_service.presentation.dto.response.CreateCategoryResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

	private final CategoryRepository categoryRepository;

	@Transactional
	// todo customException처리
	public CreateCategoryResponse createCategory(CreateCategoryRequest request) {
		//todo Admin Role만 가능하게 처리

		if (categoryRepository.existsByName(request.categoryName())) {
			throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
		}

		Category category = Category.of(request.categoryName());
		Category saved = categoryRepository.save(category);
		return CreateCategoryResponse.from(saved);
	}
}
