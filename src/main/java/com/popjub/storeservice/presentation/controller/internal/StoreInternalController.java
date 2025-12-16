package com.popjub.storeservice.presentation.controller.internal;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.popjub.common.enums.SuccessCode;
import com.popjub.common.response.ApiResponse;
import com.popjub.storeservice.application.dto.command.UpdateRatingCommand;
import com.popjub.storeservice.application.dto.result.SearchTimeSlotInternalResult;
import com.popjub.storeservice.application.service.StoreService;
import com.popjub.storeservice.application.service.TimeSlotService;
import com.popjub.storeservice.domain.entity.TimeSlotStatus;
import com.popjub.storeservice.presentation.dto.request.UpdateRatingRequest;
import com.popjub.storeservice.presentation.dto.response.SearchTimeSlotInternalResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/stores")
public class StoreInternalController {

	private final StoreService storeService;
	private final TimeSlotService timeSlotService;


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

	@GetMapping("/checkin/validate")
	public boolean validateCheckin(
		@RequestParam("storeId") UUID storeId,
		@RequestParam("timeslotId") UUID timeslotId,
		@RequestParam("userId") Long userId
	){
		return storeService.validateCheckin(storeId, timeslotId, userId);
	}

	@GetMapping("/{timeslotId}")
	public SearchTimeSlotInternalResponse getTimeslot(@PathVariable UUID timeslotId) {
		SearchTimeSlotInternalResult result = timeSlotService.getTimeSlotInternal(timeslotId);
		return SearchTimeSlotInternalResponse.from(result);
	}

	@PostMapping("/timeslot/update-status")
	void updateTimeSlotStatus(
		@RequestParam("timeslotId") UUID timeslotId,
		@RequestParam("status") TimeSlotStatus status
	){
		timeSlotService.statusUpdate(timeslotId, status);
	}

	@GetMapping("/{storeId}/exists")
	public boolean existsStore(@PathVariable("storeId") UUID storeId){
		return storeService.existsStore(storeId);
	}
	/**
	 * consumer - 이벤트 받는 곳
	 * review -> store kafka로 통신
	 * 통신하는 방식 producer(review)  consumer(store)
	 */
}
