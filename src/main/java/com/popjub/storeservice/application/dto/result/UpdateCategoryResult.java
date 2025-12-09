package com.popjub.storeservice.application.dto.result;

import com.popjub.storeservice.domain.entity.Category;

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
