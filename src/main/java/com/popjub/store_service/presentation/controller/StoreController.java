package com.popjub.store_service.presentation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.popjub.common.enums.SuccessCode;
import com.popjub.common.response.ApiResponse;
import com.popjub.store_service.application.dto.command.CreateStoreCommand;
import com.popjub.store_service.application.dto.command.CreateTimeRuleCommand;
import com.popjub.store_service.application.dto.result.CreateStoreResult;
import com.popjub.store_service.application.service.StoreService;
import com.popjub.store_service.presentation.dto.request.CreateStoreRequest;
import com.popjub.store_service.presentation.dto.response.CreateStoreResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
public class StoreController {

	private final StoreService storeService;

	@PostMapping
	public ApiResponse<CreateStoreResponse> createStore(
		@Valid @RequestBody CreateStoreRequest request) {
		CreateStoreCommand storeCommand = request.toStoreCommand();
		List<CreateTimeRuleCommand> timeRuleCommand = request.toTimeRulesCommand();
		List<Long> categoryIds = request.toCategoryIds();

		CreateStoreResult result = storeService.createStore(storeCommand, timeRuleCommand, categoryIds);

		CreateStoreResponse response = CreateStoreResponse.from(result);
		return ApiResponse.of(SuccessCode.CREATED, response);
	}
}
