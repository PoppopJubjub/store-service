package com.popjub.store_service.presentation.controller;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.popjub.common.enums.SuccessCode;
import com.popjub.common.response.ApiResponse;
import com.popjub.common.response.PageResponse;
import com.popjub.store_service.application.dto.command.CreateTimeSlotCommand;
import com.popjub.store_service.application.dto.result.CreateTimeSlotResult;
import com.popjub.store_service.application.dto.result.SearchTimeSlotResult;
import com.popjub.store_service.application.service.TimeSlotService;
import com.popjub.store_service.presentation.dto.request.CreateTimeSlotRequest;
import com.popjub.store_service.presentation.dto.response.CreateTimeSlotResponse;
import com.popjub.store_service.presentation.dto.response.SearchAllTimeSlotResponse;
import com.popjub.store_service.presentation.dto.response.SearchTimeSlotResponse;
import com.popjub.store_service.presentation.dto.response.TimeSlotSummaryResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
public class TimeSlotController {

	private final TimeSlotService timeSlotService;

	@PostMapping("/{storeId}/timeslots")
	public ApiResponse<CreateTimeSlotResponse> createTimeSlots(
		@PathVariable UUID storeId,
		@Valid @RequestBody CreateTimeSlotRequest request
	){
		CreateTimeSlotCommand command = request.toCommand();
		CreateTimeSlotResult result = timeSlotService.createTimeslots(storeId, command);
		CreateTimeSlotResponse response = CreateTimeSlotResponse.from(result);

		return ApiResponse.of(SuccessCode.CREATED, response);
	}

	@GetMapping("/{storeId}/timeslots/{timeSlotId}")
	public ApiResponse<SearchTimeSlotResponse> getTimeSlots(
		@PathVariable UUID storeId,
		@PathVariable UUID timeSlotId
	){
		SearchTimeSlotResult result = timeSlotService.getTimeSlot(timeSlotId);
		SearchTimeSlotResponse response = SearchTimeSlotResponse.from(result);
		return ApiResponse.of(SuccessCode.OK, response);
	}

	@GetMapping("/timeslots")
	public ApiResponse<PageResponse<SearchAllTimeSlotResponse>> getAllTimeSlots(
		@PageableDefault(
			size = 10,
			sort = "storeName",
			direction = Sort.Direction.ASC
		)Pageable pageable
	){
		Page<SearchTimeSlotResult> result = timeSlotService.getAllTimeSlots(pageable);
		Page<SearchAllTimeSlotResponse> response = result.map(SearchAllTimeSlotResponse::from);
		PageResponse<SearchAllTimeSlotResponse> pageResponse = PageResponse.from(response);
		return ApiResponse.of(SuccessCode.OK, pageResponse);
	}

	@GetMapping("/{storeId}/timeslots")
	public ApiResponse<PageResponse<TimeSlotSummaryResponse>> getStoreTimeSlotsByDate(
		@PathVariable UUID storeId,
		@RequestParam LocalDate date,
		@PageableDefault(
			size = 10,
			sort = "startTime",
			direction = Sort.Direction.ASC
		) Pageable pageable
	){
		Page<SearchTimeSlotResult> result = timeSlotService.getStoreTimeSlots(storeId, date, pageable);
		Page<TimeSlotSummaryResponse> response = result.map(TimeSlotSummaryResponse::from);
		PageResponse<TimeSlotSummaryResponse> pageResponse = PageResponse.from(response);
		return ApiResponse.of(SuccessCode.OK, pageResponse);
	}
}
