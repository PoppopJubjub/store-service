package com.popjub.store_service.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.popjub.store_service.application.dto.command.CreateTimeSlotCommand;
import com.popjub.store_service.application.dto.result.CreateTimeSlotResult;
import com.popjub.store_service.application.validation.TimeSlotValidator;
import com.popjub.store_service.domain.entity.Store;
import com.popjub.store_service.domain.entity.StoreTime;
import com.popjub.store_service.domain.entity.TimeSlot;
import com.popjub.store_service.domain.repository.StoreRepository;
import com.popjub.store_service.domain.repository.StoreTimeRepository;
import com.popjub.store_service.domain.repository.TimeslotRepository;
import com.popjub.store_service.exception.StoreCustomException;
import com.popjub.store_service.exception.StoreErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimeSlotService {

	private final TimeslotRepository timeslotRepository;
	private final StoreTimeRepository storeTimeRepository;
	private final StoreRepository storeRepository;
	private final TimeSlotValidator timeSlotValidator;

	@Transactional
	public CreateTimeSlotResult createTimeslots(UUID storeId, CreateTimeSlotCommand command) {

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_STORE));

		StoreTime storeTime = storeTimeRepository
			.findByStoreAndDate(store, command.date())
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_STORE_TIME));

		timeSlotValidator.validate(storeTime);
		List<TimeSlot> timeSlots = command.createTimeslots(store, storeTime);
		List<TimeSlot> savedTimeSlots = timeslotRepository.saveAll(timeSlots);
		return CreateTimeSlotResult.from(savedTimeSlots);
	}
}
