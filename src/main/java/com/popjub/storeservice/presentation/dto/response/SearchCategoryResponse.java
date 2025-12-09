package com.popjub.storeservice.presentation.dto.response;

import com.popjub.storeservice.application.dto.result.SearchCategoryResult;

public record SearchCategoryResponse(
	Long categoryId,
	String categoryName
) {
	public static  SearchCategoryResponse from(SearchCategoryResult result) {
		return new SearchCategoryResponse(
			result.categoryId(),
			result.categoryName()
		);
	}
}
