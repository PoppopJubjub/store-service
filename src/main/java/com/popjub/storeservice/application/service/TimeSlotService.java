package com.popjub.storeservice.application.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.popjub.storeservice.application.dto.command.CreateTimeSlotCommand;
import com.popjub.storeservice.application.dto.command.UpdateTimeSlotCommand;
import com.popjub.storeservice.application.dto.result.CreateTimeSlotResult;
import com.popjub.storeservice.application.dto.result.GetRemainingResult;
import com.popjub.storeservice.application.dto.result.SearchTimeSlotInternalResult;
import com.popjub.storeservice.application.dto.result.SearchTimeSlotResult;
import com.popjub.storeservice.application.dto.result.UpdateTimeSlotResult;
import com.popjub.storeservice.application.event.TimeSlotCloseEvent;
import com.popjub.storeservice.application.port.ReservationServicePort;
import com.popjub.storeservice.application.validation.StoreValidator;
import com.popjub.storeservice.application.validation.TimeSlotValidator;
import com.popjub.storeservice.domain.entity.Store;
import com.popjub.storeservice.domain.entity.StoreTime;
import com.popjub.storeservice.domain.entity.TimeSlot;
import com.popjub.storeservice.domain.entity.TimeSlotStatus;
import com.popjub.storeservice.domain.repository.StoreRepository;
import com.popjub.storeservice.domain.repository.StoreTimeRepository;
import com.popjub.storeservice.domain.repository.TimeSlotRepository;
import com.popjub.storeservice.exception.StoreCustomException;
import com.popjub.storeservice.exception.StoreErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimeSlotService {

	private final TimeSlotRepository timeslotRepository;
	private final StoreTimeRepository storeTimeRepository;
	private final StoreRepository storeRepository;
	private final TimeSlotValidator timeSlotValidator;
	private final StoreValidator storeValidator;
	private final ReservationServicePort reservationServicePort;
	private final ApplicationEventPublisher publisher;

	@Transactional
	public CreateTimeSlotResult createTimeslots(UUID storeId, CreateTimeSlotCommand command, Long currentUserId, List<String> role) {

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_STORE));

		storeValidator.validateManagerOrAdmin(store, currentUserId, role);

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

	public Page<GetRemainingResult> getStoreTimeSlots(UUID storeId, LocalDate date, Pageable pageable){
		Page<TimeSlot> timeSlotByStorePage = timeslotRepository.findAllByStore_StoreIdAndDate(storeId, date, pageable);

		//페이지에 있는 타임슬롯들의 ID 뽑아서 리스트로 만들기
		List<UUID> timeslotIds = timeSlotByStorePage.stream()
			.map(TimeSlot::getTimeslotId)
			.toList();

		//예약측으로 조회 요청
		Map<UUID, Integer> capacities = reservationServicePort.getCapacities(timeslotIds);

		//결과 매핑
		return timeSlotByStorePage.map(
			timeSlot -> {
				Integer remaining = capacities.getOrDefault(
					timeSlot.getTimeslotId(),
					timeSlot.getCapacity()
				);
				return GetRemainingResult.from(timeSlot, remaining);
			}
		);
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
	public UpdateTimeSlotResult updateTimeSlots(UUID timeSlotId, UpdateTimeSlotCommand command, Long currentUserId, List<String> role) {
		TimeSlot timeSlot = timeslotRepository.findById(timeSlotId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_TIME_SLOT));
		//상태 변경
		timeSlotValidator.validateUpdateTimeSlot(timeSlot, currentUserId, role);

		switch (command.status()){
			case AVAILABLE -> timeSlot.makeAvailable();
			case CLOSED -> timeSlot.close();
			case FULL -> timeSlot.makeFull();
		}

		return UpdateTimeSlotResult.from(timeSlot);
	}

	@Transactional
	public void deleteTimeSlot(UUID timeSlotId, Long currentUserId, List<String> role) {
		TimeSlot timeSlot = timeslotRepository.findById(timeSlotId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_TIME_SLOT));

		timeSlotValidator.validateUpdateTimeSlot(timeSlot, currentUserId, role);

		timeSlot.softDelete(currentUserId);
	}

	public SearchTimeSlotInternalResult getTimeSlotInternal(UUID timeSlotId){
		TimeSlot timeSlot = timeslotRepository.findById(timeSlotId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_TIME_SLOT));

		return  SearchTimeSlotInternalResult.from(timeSlot);
	}

	@Transactional
	public void closeAndSend(LocalDateTime now){
		List<UUID> closedIds = timeslotRepository.closedUpdate(now);
		if(closedIds.isEmpty()){
			return;
		}
		publisher.publishEvent(new TimeSlotCloseEvent(closedIds));
	}

	@Transactional
	public void statusUpdate(UUID timeslotId, TimeSlotStatus status) {
		TimeSlot timeSlot = timeslotRepository.findById(timeslotId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_TIME_SLOT));
		//FULL인데 FULL 요청이 들어오거나 CLOSED로 요청이 들어오면 그냥 리턴
		if(status == timeSlot.getStatus()){
			return;
		}
		if(timeSlot.getStatus() == TimeSlotStatus.CLOSED){
			return;
		}
		switch (status) {
			case FULL -> timeSlot.makeFull();
			case AVAILABLE -> timeSlot.makeAvailable();
			default -> throw new StoreCustomException(StoreErrorCode.INVALID_TIMESLOT_STATUS);
		}
	}
}
