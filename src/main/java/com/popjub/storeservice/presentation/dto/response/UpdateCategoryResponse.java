package com.popjub.storeservice.presentation.dto.response;

import com.popjub.storeservice.application.dto.result.UpdateCategoryResult;

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
