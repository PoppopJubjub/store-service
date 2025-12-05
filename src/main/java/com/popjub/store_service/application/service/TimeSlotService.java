package com.popjub.store_service.application.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.popjub.store_service.application.dto.command.CreateTimeSlotCommand;
import com.popjub.store_service.application.dto.result.CreateTimeSlotResult;
import com.popjub.store_service.application.dto.result.SearchTimeSlotResult;
import com.popjub.store_service.application.validation.TimeSlotValidator;
import com.popjub.store_service.domain.entity.Store;
import com.popjub.store_service.domain.entity.StoreTime;
import com.popjub.store_service.domain.entity.TimeSlot;
import com.popjub.store_service.domain.repository.StoreRepository;
import com.popjub.store_service.domain.repository.StoreTimeRepository;
import com.popjub.store_service.domain.repository.TimeSlotRepository;
import com.popjub.store_service.exception.StoreCustomException;
import com.popjub.store_service.exception.StoreErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimeSlotService {

	private final TimeSlotRepository timeslotRepository;
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

	public SearchTimeSlotResult getTimeSlot(UUID timeSlotId){
		TimeSlot timeSlot = timeslotRepository.findById(timeSlotId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_TIME_SLOT));
		return SearchTimeSlotResult.from(timeSlot);
	}

	public Page<SearchTimeSlotResult> getAllTimeSlots(Pageable pageable){
		Page<TimeSlot> timeSlotPage = timeslotRepository.findAll(pageable);
		return timeSlotPage.map(SearchTimeSlotResult::from);
	}

	public Page<SearchTimeSlotResult> getStoreTimeSlots(UUID storeId, LocalDate date, Pageable pageable){
		Page<TimeSlot> timeSlotByStorePage = timeslotRepository.findAllByStore_StoreIdAndDate(storeId, date, pageable);
		return timeSlotByStorePage.map(SearchTimeSlotResult::from);
	}
}
