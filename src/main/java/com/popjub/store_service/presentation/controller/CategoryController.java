package com.popjub.store_service.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.popjub.common.enums.SuccessCode;
import com.popjub.common.response.ApiResponse;
import com.popjub.common.response.PageResponse;
import com.popjub.store_service.application.dto.command.CreateCategoryCommand;
import com.popjub.store_service.application.dto.result.CreateCategoryResult;
import com.popjub.store_service.application.dto.result.SearchCategoryResult;
import com.popjub.store_service.application.service.CategoryService;
import com.popjub.store_service.presentation.dto.request.CreateCategoryRequest;
import com.popjub.store_service.presentation.dto.response.CreateCategoryResponse;
import com.popjub.store_service.presentation.dto.response.SearchCategoryResponse;

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
		//req -> command
		CreateCategoryCommand command = request.toCommand();
		CreateCategoryResult result = categoryService.createCategory(command);
		CreateCategoryResponse response = CreateCategoryResponse.from(result);
		return ApiResponse.of(SuccessCode.CREATED,response);
	}

	@GetMapping
	public ApiResponse<PageResponse<SearchCategoryResponse>> searchCategory(
		@PageableDefault(
		size = 10,
		sort = "createdAt",
		direction = Sort.Direction.DESC
		)Pageable pageable
	){
		Page<SearchCategoryResult> result = categoryService.searchCategory(pageable);
		Page<SearchCategoryResponse> response = result.map(SearchCategoryResponse::from);
		PageResponse<SearchCategoryResponse> pageResponse = PageResponse.from(response);
		return ApiResponse.of(SuccessCode.OK, pageResponse);
	}
	@GetMapping("/{categoryId}")
	public ApiResponse<SearchCategoryResponse> searchCategoryDetails(
		@PathVariable Long categoryId
	){
		SearchCategoryResult result = categoryService.searchCategoryDetail(categoryId);
		SearchCategoryResponse response = SearchCategoryResponse.from(result);
		return ApiResponse.of(SuccessCode.OK, response);
	}
}
