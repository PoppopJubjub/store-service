package com.popjub.store_service.presentation.dto.request;

import com.popjub.store_service.application.dto.command.CreateCategoryCommand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(
	@NotBlank(message = "카테고리 이름은 필수입니다.")
	@Size(max = 50, message = "카테고리 이름은 최대 50자까지 가능합니다.")
	String categoryName
) {
	public CreateCategoryCommand toCommand(){
		return new CreateCategoryCommand(categoryName);
	}
}
