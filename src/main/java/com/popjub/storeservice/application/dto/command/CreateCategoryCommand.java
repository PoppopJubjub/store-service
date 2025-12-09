package com.popjub.storeservice.application.dto.command;

import com.popjub.storeservice.domain.entity.Category;

public record CreateCategoryCommand(
	String categoryName
) {
	public Category toEntity() {
		return Category.of(categoryName);
	}
}
