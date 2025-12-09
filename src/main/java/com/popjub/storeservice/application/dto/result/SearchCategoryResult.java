package com.popjub.storeservice.application.dto.result;

import com.popjub.storeservice.domain.entity.Category;

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
