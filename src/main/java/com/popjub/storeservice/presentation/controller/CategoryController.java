package com.popjub.storeservice.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.popjub.common.annotation.RoleCheck;
import com.popjub.common.enums.SuccessCode;
import com.popjub.common.enums.UserRole;
import com.popjub.common.response.ApiResponse;
import com.popjub.common.response.PageResponse;
import com.popjub.storeservice.application.dto.command.CreateCategoryCommand;
import com.popjub.storeservice.application.dto.command.UpdateCategoryCommand;
import com.popjub.storeservice.application.dto.result.CreateCategoryResult;
import com.popjub.storeservice.application.dto.result.SearchCategoryResult;
import com.popjub.storeservice.application.dto.result.UpdateCategoryResult;
import com.popjub.storeservice.application.service.CategoryService;
import com.popjub.storeservice.presentation.dto.request.CreateCategoryRequest;
import com.popjub.storeservice.presentation.dto.request.UpdateCategoryRequest;
import com.popjub.storeservice.presentation.dto.response.CreateCategoryResponse;
import com.popjub.storeservice.presentation.dto.response.SearchCategoryResponse;
import com.popjub.storeservice.presentation.dto.response.UpdateCategoryResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

	private final CategoryService categoryService;


	@RoleCheck(UserRole.ADMIN)
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

	@RoleCheck(UserRole.ADMIN)
	@PutMapping("/{categoryId}")
	public ApiResponse<UpdateCategoryResponse> updateCategory(
		@PathVariable Long categoryId,
		@RequestBody @Valid UpdateCategoryRequest request
	) {
		UpdateCategoryCommand command = request.toCommand();
		UpdateCategoryResult result = categoryService.updateCategory(categoryId, command);
		UpdateCategoryResponse response = UpdateCategoryResponse.from(result);
		return ApiResponse.of(SuccessCode.OK, response);
	}

	@RoleCheck(UserRole.ADMIN)
	@DeleteMapping("/{categoryId}")
	public ApiResponse<String> deleteCategory(
		@PathVariable Long categoryId
		//todo CurrentUser 추가
	){
		categoryService.deleteCategory(categoryId);
		return ApiResponse.of(SuccessCode.OK, "");
	}
}
