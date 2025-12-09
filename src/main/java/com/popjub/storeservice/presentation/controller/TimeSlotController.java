package com.popjub.storeservice.presentation.controller;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import com.popjub.common.enums.SuccessCode;
import com.popjub.common.response.ApiResponse;
import com.popjub.common.response.PageResponse;
import com.popjub.storeservice.application.dto.command.CreateTimeSlotCommand;
import com.popjub.storeservice.application.dto.command.UpdateTimeSlotCommand;
import com.popjub.storeservice.application.dto.result.CreateTimeSlotResult;
import com.popjub.storeservice.application.dto.result.SearchTimeSlotResult;
import com.popjub.storeservice.application.dto.result.UpdateTimeSlotResult;
import com.popjub.storeservice.application.service.TimeSlotService;
import com.popjub.storeservice.presentation.dto.request.CreateTimeSlotRequest;
import com.popjub.storeservice.presentation.dto.request.UpdateTimeSlotRequest;
import com.popjub.storeservice.presentation.dto.response.CreateTimeSlotResponse;
import com.popjub.storeservice.presentation.dto.response.SearchTimeSlotResponse;
import com.popjub.storeservice.presentation.dto.response.UpdateTimeSlotResponse;

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
	public ApiResponse<PageResponse<SearchTimeSlotResponse>> getAllTimeSlots(
		@PageableDefault(size = 10) Pageable pageable
	) {
		Pageable sortedPageable = PageRequest.of(
			pageable.getPageNumber(),
			pageable.getPageSize(),
			Sort.by(
				Sort.Order.asc("date"),
				Sort.Order.asc("store.name"),
				Sort.Order.asc("startTime")
			)
		);
		Page<SearchTimeSlotResult> result = timeSlotService.getAllTimeSlots(sortedPageable);
		Page<SearchTimeSlotResponse> responsePage = result.map(SearchTimeSlotResponse::from);
		PageResponse<SearchTimeSlotResponse> pageResponse = PageResponse.from(responsePage);
		return ApiResponse.of(SuccessCode.OK, pageResponse);
	}


	@GetMapping("/{storeId}/timeslots")
	public ApiResponse<PageResponse<SearchTimeSlotResponse>> getStoreTimeSlotsByDate(
		@PathVariable UUID storeId,
		@RequestParam LocalDate date,
		@PageableDefault(size = 10) Pageable pageable
	) {
		Pageable sortedPageable = PageRequest.of(
			pageable.getPageNumber(),
			pageable.getPageSize(),
			Sort.by(
				Sort.Order.asc("date"),
				Sort.Order.asc("startTime")
			)
		);
		Page<SearchTimeSlotResult> result =
			timeSlotService.getStoreTimeSlots(storeId, date, sortedPageable);
		Page<SearchTimeSlotResponse> responsePage = result.map(SearchTimeSlotResponse::from);
		return ApiResponse.of(SuccessCode.OK, PageResponse.from(responsePage));
	}

	@PutMapping("/{storeId}/timeslots/{timeslotId}")
	public ApiResponse<UpdateTimeSlotResponse> updateTimeSlots(
		@PathVariable UUID storeId,
		@PathVariable UUID timeslotId,
		@Valid @RequestBody UpdateTimeSlotRequest request
	){
		UpdateTimeSlotCommand command = request.toCommand();
		UpdateTimeSlotResult result = timeSlotService.updateTimeSlots(timeslotId, command);
		UpdateTimeSlotResponse response = UpdateTimeSlotResponse.from(result);
		return ApiResponse.of(SuccessCode.OK, response);
	}

	@DeleteMapping("/timeslots/{timeSlotId}")
	public ApiResponse<String> deleteTimeSlot(
		@PathVariable UUID timeSlotId
	){
		timeSlotService.deleteTimeSlot(timeSlotId);
		return ApiResponse.of(SuccessCode.OK,"");
	}
}
