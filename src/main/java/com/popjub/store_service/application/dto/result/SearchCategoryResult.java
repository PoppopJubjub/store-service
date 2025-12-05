package com.popjub.store_service.application.dto.result;

import com.popjub.store_service.domain.entity.Category;

public record SearchCategoryResult(
	Long categoryId,
	String categoryName
){
	public static SearchCategoryResult from(Category category){
		return new SearchCategoryResult(
			category.getCategoryId(),
			category.getCategoryName()
		);
	}
}
