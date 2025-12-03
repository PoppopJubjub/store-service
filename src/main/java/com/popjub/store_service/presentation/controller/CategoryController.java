package com.popjub.store_service.presentation.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.popjub.common.enums.SuccessCode;
import com.popjub.common.response.ApiResponse;
import com.popjub.store_service.application.service.CategoryService;
import com.popjub.store_service.presentation.dto.request.CreateCategoryRequest;
import com.popjub.store_service.presentation.dto.response.CreateCategoryResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

	private final CategoryService categoryService;


	@PostMapping
	public ApiResponse<CreateCategoryResponse> createCategory(
		@Valid @RequestBody CreateCategoryRequest request){
		CreateCategoryResponse response = categoryService.createCategory(request);
		return ApiResponse.of(SuccessCode.CREATED,response);


	}
}
