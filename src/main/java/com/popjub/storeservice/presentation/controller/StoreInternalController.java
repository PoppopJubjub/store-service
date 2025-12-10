package com.popjub.storeservice.presentation.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.popjub.common.enums.SuccessCode;
import com.popjub.common.response.ApiResponse;
import com.popjub.storeservice.application.dto.command.UpdateRatingCommand;
import com.popjub.storeservice.application.service.StoreService;
import com.popjub.storeservice.presentation.dto.request.UpdateRatingRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/stores")
public class StoreInternalController {

	private final StoreService storeService;

	@PostMapping("/{storeId}/reviews/rating")
	public ApiResponse<String> increaseRating(
		@Valid @RequestBody UpdateRatingRequest request,
		@PathVariable UUID storeId
	){
		UpdateRatingCommand command = request.toCommand(request);
		storeService.increaseRating(command);
		return ApiResponse.of(SuccessCode.OK,"");
	}

	@PostMapping("/{storeId}/reviews/rating/delete")
	public ApiResponse<String> decreaseRating(
		@Valid @RequestBody UpdateRatingRequest request,
		@PathVariable UUID storeId
	){
		UpdateRatingCommand command = request.toCommand(request);
		storeService.decreaseRating(command);
		return ApiResponse.of(SuccessCode.OK,"");
	}
	/**
	 * consumer - 이벤트 받는 곳
	 * review -> store kafka로 통신
	 * 통신하는 방식 producer(review)  consumer(store)
	 */
}
