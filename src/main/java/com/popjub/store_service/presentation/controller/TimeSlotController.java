package com.popjub.store_service.presentation.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.popjub.common.enums.SuccessCode;
import com.popjub.common.response.ApiResponse;
import com.popjub.store_service.application.dto.command.CreateTimeSlotCommand;
import com.popjub.store_service.application.dto.result.CreateTimeSlotResult;
import com.popjub.store_service.application.service.TimeSlotService;
import com.popjub.store_service.presentation.dto.request.CreateTimeSlotRequest;
import com.popjub.store_service.presentation.dto.response.CreateTimeSlotResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores/{storeId}")
public class TimeSlotController {

	private final TimeSlotService timeSlotService;

	@PostMapping("/timeslots")
	public ApiResponse<CreateTimeSlotResponse> createTimeSlots(
		@PathVariable UUID storeId,
		@Valid @RequestBody CreateTimeSlotRequest request
	){
		CreateTimeSlotCommand command = request.toCommand();
		CreateTimeSlotResult result = timeSlotService.createTimeslots(storeId, command);
		CreateTimeSlotResponse response = CreateTimeSlotResponse.from(result);

		return ApiResponse.of(SuccessCode.CREATED, response);
	}

}
