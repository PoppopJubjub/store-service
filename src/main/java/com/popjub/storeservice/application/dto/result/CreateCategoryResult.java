package com.popjub.storeservice.application.dto.result;

import com.popjub.storeservice.domain.entity.Category;

public record CreateCategoryResult(
	Long categoryId
) {
	public static CreateCategoryResult from(Category category){
		return new CreateCategoryResult(category.getCategoryId());
	}
}
