package com.popjub.store_service.application.dto.result;

import com.popjub.store_service.domain.entity.Category;

public record UpdateCategoryResult(
	Long categoryId,
	String categoryName
) {
	public static UpdateCategoryResult from(Category category){
		return new UpdateCategoryResult(
			category.getCategoryId(),
			category.getCategoryName()
		);
	}
}
