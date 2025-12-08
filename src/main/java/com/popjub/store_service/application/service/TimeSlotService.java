package com.popjub.store_service.application.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.popjub.store_service.application.dto.command.CreateTimeSlotCommand;
import com.popjub.store_service.application.dto.command.UpdateTimeSlotCommand;
import com.popjub.store_service.application.dto.result.CreateTimeSlotResult;
import com.popjub.store_service.application.dto.result.SearchTimeSlotResult;
import com.popjub.store_service.application.dto.result.UpdateTimeSlotResult;
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

	//스토어 타임 수정에 따른 자동 타임슬롯 재정의 메서드
	@Transactional
	public void RegenerateTimeSlots(Store store, LocalDate date){
		List<TimeSlot> existing = timeslotRepository.findAllByStoreAndDate(store, date);
		
		if(existing.isEmpty()){
			throw new StoreCustomException(StoreErrorCode.NOT_FOUND_TIME_SLOT);
		}

		//TimeSlot의 interval과 capacity 가져오기
		TimeSlot baseSlot = existing.get(0);
		Integer interval = baseSlot.getInterval();
		Integer capacity = baseSlot.getCapacity();
		
		//기존 타임슬롯 제거
		timeslotRepository.deleteAllByStoreAndDate(store, date);

		//변경된 운영시간 조회
		StoreTime storeTime = storeTimeRepository.findByStoreAndDate(store, date)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_STORE_TIME));

		//운영시간 기준으로 새 타임슬롯 생성
		CreateTimeSlotCommand command = new CreateTimeSlotCommand(
			date,
			interval,
			capacity
		);

		List<TimeSlot> newTimeSlots = command.createTimeslots(store, storeTime);
		timeslotRepository.saveAll(newTimeSlots);
	}

	@Transactional
	public UpdateTimeSlotResult updateTimeSlots(UUID timeSlotId, UpdateTimeSlotCommand command) {
		TimeSlot timeSlot = timeslotRepository.findById(timeSlotId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_TIME_SLOT));
		//상태 변경
		switch (command.status()){
			case AVAILABLE -> timeSlot.makeAvailable();
			case CLOSED -> timeSlot.close();
			case FULL -> timeSlot.makeFull();
		}

		return UpdateTimeSlotResult.from(timeSlot);
	}
}
