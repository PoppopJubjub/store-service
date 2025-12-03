package com.popjub.store_service.application.dto.result;

import com.popjub.store_service.domain.entity.Category;

public record CreateCategoryResult(
	Long categoryId
) {
	public static CreateCategoryResult from(Category category){
		return new CreateCategoryResult(category.getCategoryId());
	}
}
