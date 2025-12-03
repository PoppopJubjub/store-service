package com.popjub.store_service.application.dto.command;

import com.popjub.store_service.domain.entity.Category;

public record CreateCategoryCommand(
	String categoryName
) {
	public Category toEntity() {
		return Category.of(categoryName);
	}
}
