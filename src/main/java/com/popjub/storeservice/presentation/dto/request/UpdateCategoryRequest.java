package com.popjub.storeservice.presentation.dto.request;

import com.popjub.storeservice.application.dto.command.UpdateCategoryCommand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCategoryRequest(
	@NotBlank(message = "카테고리 이름은 필수입니다.")
	@Size(max = 50, message = "카테고리 이름은 최대 50자까지 가능합니다.")
	String categoryName
) {
	public UpdateCategoryCommand toCommand(){
		return new UpdateCategoryCommand(categoryName);
	}
}
