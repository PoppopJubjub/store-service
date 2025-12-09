package com.popjub.storeservice.presentation.dto.response;

import com.popjub.storeservice.application.dto.result.CreateCategoryResult;

public record CreateCategoryResponse(
	Long categoryId
) {
	public static CreateCategoryResponse from(CreateCategoryResult result) {
		return new CreateCategoryResponse(
			result.categoryId()
		);
	}
}
