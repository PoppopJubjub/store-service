package com.popjub.store_service.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.popjub.store_service.application.dto.command.CreateCategoryCommand;
import com.popjub.store_service.application.dto.command.UpdateCategoryCommand;
import com.popjub.store_service.application.dto.result.CreateCategoryResult;
import com.popjub.store_service.application.dto.result.SearchCategoryResult;
import com.popjub.store_service.application.dto.result.UpdateCategoryResult;
import com.popjub.store_service.domain.entity.Category;
import com.popjub.store_service.domain.repository.CategoryRepository;
import com.popjub.store_service.exception.StoreCustomException;
import com.popjub.store_service.exception.StoreErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

	private final CategoryRepository categoryRepository;

	@Transactional
	public CreateCategoryResult createCategory(CreateCategoryCommand command) {
		// todo : Admin Role만 가능하게 처리

		if (categoryRepository.existsByName(command.categoryName())) {
			throw new StoreCustomException(StoreErrorCode.ALREADY_EXISTS_CATEGORY);
		}

		Category category = command.toEntity();
		Category saved = categoryRepository.save(category);
		return CreateCategoryResult.from(saved);
	}

	public Page<SearchCategoryResult> searchCategory(Pageable pageable) {
		Page<Category> categoryPage = categoryRepository.findAll(pageable);
		return categoryPage.map(SearchCategoryResult::from);
	}

	public SearchCategoryResult searchCategoryDetail(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_CATEGORY));
		return SearchCategoryResult.from(category);
	}

	@Transactional
	public UpdateCategoryResult updateCategory(Long CategoryId, UpdateCategoryCommand command) {
		Category category = categoryRepository.findById(CategoryId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_CATEGORY));

		if(!category.getCategoryName().equals(command.categoryName())&& categoryRepository.existsByName(command.categoryName())) {
			throw new StoreCustomException(StoreErrorCode.ALREADY_EXISTS_CATEGORY);
		}

		category.updateCategoryName(command.categoryName());
		return UpdateCategoryResult.from(category);
	}
}
