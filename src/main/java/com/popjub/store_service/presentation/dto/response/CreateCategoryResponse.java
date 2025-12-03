package com.popjub.store_service.presentation.dto.response;

import com.popjub.store_service.application.dto.result.CreateCategoryResult;
import com.popjub.store_service.domain.entity.Category;

public record CreateCategoryResponse(
	Long categoryId
) {
	public static CreateCategoryResponse from(CreateCategoryResult result) {
		return new CreateCategoryResponse(
			result.categoryId()
		);
	}
}
