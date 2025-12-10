package com.popjub.storeservice.presentation.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.popjub.common.annotation.CurrentUser;
import com.popjub.common.annotation.RoleCheck;
import com.popjub.common.enums.SuccessCode;
import com.popjub.common.enums.UserRole;
import com.popjub.common.response.ApiResponse;
import com.popjub.common.response.PageResponse;
import com.popjub.storeservice.application.dto.command.CreateStoreCommand;
import com.popjub.storeservice.application.dto.command.CreateTimeRuleCommand;
import com.popjub.storeservice.application.dto.command.UpdateStoreCommand;
import com.popjub.storeservice.application.dto.command.UpdateStoreTimeCommand;
import com.popjub.storeservice.application.dto.result.CreateStoreResult;
import com.popjub.storeservice.application.dto.result.SearchStoreResult;
import com.popjub.storeservice.application.dto.result.UpdateStoreResult;
import com.popjub.storeservice.application.dto.result.UpdateStoreTimeResult;
import com.popjub.storeservice.application.service.StoreService;
import com.popjub.storeservice.presentation.dto.request.CreateStoreRequest;
import com.popjub.storeservice.presentation.dto.request.UpdateStoreRequest;
import com.popjub.storeservice.presentation.dto.request.UpdateStoreTimeRequest;
import com.popjub.storeservice.presentation.dto.response.CreateStoreResponse;
import com.popjub.storeservice.presentation.dto.response.SearchStoreResponse;
import com.popjub.storeservice.presentation.dto.response.UpdateStoreResponse;
import com.popjub.storeservice.presentation.dto.response.UpdateStoreTimeResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
public class StoreController {

	private final StoreService storeService;

	@RoleCheck({UserRole.ADMIN, UserRole.STORE_MANAGER})
	@PostMapping
	public ApiResponse<CreateStoreResponse> createStore(
		@Valid @RequestBody CreateStoreRequest request,
		@CurrentUser Long userId) {
		CreateStoreCommand storeCommand = request.toStoreCommand();
		List<CreateTimeRuleCommand> timeRuleCommand = request.toTimeRulesCommand();
		List<Long> categoryIds = request.toCategoryIds();

		CreateStoreResult result = storeService.createStore(storeCommand, timeRuleCommand, categoryIds);

		CreateStoreResponse response = CreateStoreResponse.from(result);
		return ApiResponse.of(SuccessCode.CREATED, response);
	}

	@GetMapping
	public ApiResponse<PageResponse<SearchStoreResponse>> searchStore(
		@PageableDefault(
			size = 10,
			sort = "createdAt",
			direction = Sort.Direction.DESC
		) Pageable pageable
	) {
		Page<SearchStoreResult> result = storeService.searchStore(pageable);
		Page<SearchStoreResponse> response = result.map(SearchStoreResponse::from);
		PageResponse<SearchStoreResponse> pageResponse = PageResponse.from(response);
		return ApiResponse.of(SuccessCode.OK, pageResponse);
	}

	@GetMapping("/{storeId}")
	public ApiResponse<SearchStoreResponse> searchStoreDetails(
		@PathVariable UUID storeId
	){
		SearchStoreResult result = storeService.searchStoreDetail(storeId);
		SearchStoreResponse response = SearchStoreResponse.from(result);
		return ApiResponse.of(SuccessCode.OK, response);
	}

	@RoleCheck({UserRole.ADMIN, UserRole.STORE_MANAGER})
	@PutMapping("/{storeId}")
	public ApiResponse<UpdateStoreResponse> updateStore(
		@PathVariable UUID storeId,
		@Valid @RequestBody UpdateStoreRequest request,
		@CurrentUser Long userId
	) {
		UpdateStoreCommand command = request.toCommand();
		UpdateStoreResult result = storeService.updateStore(storeId, command, userId);
		UpdateStoreResponse response = UpdateStoreResponse.from(result);
		return ApiResponse.of(SuccessCode.OK, response);
	}

	@RoleCheck({UserRole.ADMIN, UserRole.STORE_MANAGER})
	@PutMapping("/{storeId}/store-times")
	public ApiResponse<UpdateStoreTimeResponse> updateStoreTime(
		@PathVariable UUID storeId,
		@RequestParam LocalDate date,
		@Valid @RequestBody UpdateStoreTimeRequest request
	){
		UpdateStoreTimeCommand command = request.toCommand();
		UpdateStoreTimeResult result = storeService.updateStoreTime(storeId, date, command);
		UpdateStoreTimeResponse response = UpdateStoreTimeResponse.from(result);
		return ApiResponse.of(SuccessCode.OK, response);
	}

	@RoleCheck(UserRole.STORE_MANAGER)
	@DeleteMapping("{storeId}/categories/{categoryId}")
		public ApiResponse<String> deleteStoreCategory(
			@PathVariable UUID storeId,
			@PathVariable Long categoryId
			){
		storeService.deleteStoreCategory(storeId, categoryId);
		return ApiResponse.of(SuccessCode.OK,"");
	}

	@RoleCheck({UserRole.ADMIN, UserRole.STORE_MANAGER})
	@DeleteMapping("{storeId}")
	public ApiResponse<String> deleteStore(
		@PathVariable UUID storeId
	){
		storeService.deleteStore(storeId);
		return ApiResponse.of(SuccessCode.OK,"");
	}
}
