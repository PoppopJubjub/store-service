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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimeSlotService {

	private final TimeslotRepository timeslotRepository;
	private final StoreTimeRepository storeTimeRepository;
	private final StoreRepository storeRepository;
	private final TimeSlotValidator timeSlotValidator;

	// todo customException처리
	@Transactional
	public CreateTimeSlotResult createTimeslots(UUID storeId, CreateTimeSlotCommand command) {

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스토어입니다."));

		StoreTime storeTime = storeTimeRepository
			.findByStoreAndDate(store, command.date())
			.orElseThrow(() -> new IllegalArgumentException("해당 날짜의 운영시간이 없습니다."));

		timeSlotValidator.validate(command, storeTime);
		List<TimeSlot> timeSlots = command.createTimeslots(store, storeTime);
		List<TimeSlot> savedTimeSlots = timeslotRepository.saveAll(timeSlots);
		return CreateTimeSlotResult.from(savedTimeSlots);
	}
}
