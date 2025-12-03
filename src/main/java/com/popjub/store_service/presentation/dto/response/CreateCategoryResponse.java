package com.popjub.store_service.presentation.dto.response;

import com.popjub.store_service.domain.entity.Category;

public record CreateCategoryResponse(
	Long categoryId
) {
	public static CreateCategoryResponse from(Category category){
		return new CreateCategoryResponse(category.getCategoryId());
	}
}
