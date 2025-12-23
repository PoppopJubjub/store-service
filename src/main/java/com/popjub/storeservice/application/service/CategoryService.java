package com.popjub.storeservice.application.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.popjub.storeservice.application.dto.command.CreateCategoryCommand;
import com.popjub.storeservice.application.dto.command.UpdateCategoryCommand;
import com.popjub.storeservice.application.dto.result.CreateCategoryResult;
import com.popjub.storeservice.application.dto.result.SearchCategoryResult;
import com.popjub.storeservice.application.dto.result.UpdateCategoryResult;
import com.popjub.storeservice.application.validation.StoreValidator;
import com.popjub.storeservice.domain.entity.Category;
import com.popjub.storeservice.domain.entity.StoreCategory;
import com.popjub.storeservice.domain.repository.CategoryRepository;
import com.popjub.storeservice.domain.repository.StoreCategoryRepository;
import com.popjub.storeservice.domain.repository.StoreRepository;
import com.popjub.storeservice.exception.StoreCustomException;
import com.popjub.storeservice.exception.StoreErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

	private final StoreRepository storeRepository;
	private final CategoryRepository categoryRepository;
	private final StoreCategoryRepository storeCategoryRepository;
	private final StoreValidator storeValidator;

	@Transactional
	public CreateCategoryResult createCategory(CreateCategoryCommand command) {
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
		Category category = getCategory(categoryId);
		return SearchCategoryResult.from(category);
	}

	@Transactional
	public UpdateCategoryResult updateCategory(Long CategoryId, UpdateCategoryCommand command) {
		Category category = getCategory(CategoryId);

		storeValidator.validateCategoryNameDuplication(category, command.categoryName());

		category.updateCategoryName(command.categoryName());
		return UpdateCategoryResult.from(category);
	}

	@Transactional
	public void deleteCategory(Long CategoryId, Long CurrentUserId) {
		Category category = getCategory(CategoryId);

		List<StoreCategory> allCategory = storeCategoryRepository.findAllByCategory(category);
		//해당 카테고리를 사용중인 스토어에서 스토어 카테고리 제거
		for(StoreCategory storeCategory : allCategory) {
			storeCategory.softDelete(CurrentUserId);
		}

		category.softDelete(CurrentUserId);
	}

	private Category getCategory(Long categoryId) {
		return categoryRepository.findById(categoryId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_CATEGORY));
	}
}