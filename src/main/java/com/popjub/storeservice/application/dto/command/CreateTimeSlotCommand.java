package com.popjub.storeservice.application.dto.command;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.popjub.storeservice.domain.entity.Store;
import com.popjub.storeservice.domain.entity.StoreTime;
import com.popjub.storeservice.domain.entity.TimeSlot;

public record CreateTimeSlotCommand(
	LocalDate date,
	Integer intervalMinutes,
	Integer capacity
) {
	public List<TimeSlot> createTimeslots(Store store, StoreTime storeTime) {
		List<TimeSlot> timeSlots = new ArrayList<>();

		LocalTime startTime = storeTime.getStartTime();
		LocalTime endTime = storeTime.getEndTime();

		LocalDateTime slotStartTime = LocalDateTime.of(date, startTime);
		LocalDateTime interval = LocalDateTime.of(date, endTime);

		/**
		 *	"슬롯이 종료 시간을 넘어가지 않는 경우에만 생성"
		 *   ex)
		 *      운영시간 09:00 ~ 18:00, 간격 60분이면 생성되는 시각:
		 *      09:00, 10:00, 11:00, ... , 17:00  ← 마지막 슬롯
		 */

		while (!slotStartTime.isAfter(interval.minusMinutes(intervalMinutes))) {

			// 현재 시작 시간으로 타임슬롯 엔티티 생성
			TimeSlot slot = TimeSlot.of(
				store,
				date,
				slotStartTime.toLocalTime(),
				intervalMinutes,
				capacity
			);

			timeSlots.add(slot);

			// 다음 슬롯의 시작시간으로 slotStartTime 이동 (intervalMinutes 만큼 증가)
			slotStartTime = slotStartTime.plusMinutes(intervalMinutes);
		}

		return timeSlots;
	}
}
