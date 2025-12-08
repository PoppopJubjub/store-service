package com.popjub.store_service.presentation.dto.response;

import com.popjub.store_service.application.dto.result.UpdateCategoryResult;

public record UpdateCategoryResponse(
	Long categoryId,
	String categoryName
) {
	public static  UpdateCategoryResponse from(UpdateCategoryResult result) {
		return new UpdateCategoryResponse(
			result.categoryId(),
			result.categoryName()
		);
	}
}
