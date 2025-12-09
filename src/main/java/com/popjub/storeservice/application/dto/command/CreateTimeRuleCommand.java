package com.popjub.storeservice.application.dto.command;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.popjub.storeservice.domain.entity.Store;
import com.popjub.storeservice.domain.entity.StoreTime;

public record CreateTimeRuleCommand(
	List<String> daysOfWeek,
	LocalTime startTime,
	LocalTime endTime
) {

	public List<StoreTime> createStoreTimes(Store store, LocalDate start, LocalDate end) {
		List<StoreTime> result = new ArrayList<>();

		/**
		 * 요청 받은 요일 문자열(MONDAY, TUESDAY...) 각각에 대해 처리
		 * 문자열 → DayOfWeek enum 변환
		 * start ~ end 사이에서 해당 요일에 해당되는 모든 날짜 목록 생성
		 */
		for (String day : daysOfWeek) {
			DayOfWeek dayOfWeek = DayOfWeek.valueOf(day);
			List<LocalDate> dates = resolveDatesByDayOfWeek(start, end, dayOfWeek);
			for (LocalDate date : dates) {
				result.add(StoreTime.of(
					store,
					date,
					startTime,
					endTime
				));
			}
		}

		return result;
	}

	/**
	 * start ~ end 구간 사이에서 특정 요일(targetDay)에 해당하는 모든 날짜를 찾는 메서드
	 * ex) 2025-12-01 ~ 2025-12-31 사이의 모든 MONDAY 날짜 구하기
	 */
	private List<LocalDate> resolveDatesByDayOfWeek(LocalDate start, LocalDate end, DayOfWeek targetDay) {
		List<LocalDate> result = new ArrayList<>();

		LocalDate date = start;
	/**
	 * /1) 첫 시작점(start)에서 targetDay가 나올 때까지 +1일씩 이동
	 *   start가 이미 targetDay라면 이 루프는 바로 종료됨
	 */

		while (date.getDayOfWeek() != targetDay) {
			date = date.plusDays(1);
			if (date.isAfter(end))
				return result;
		}

		/**
		 * 2) targetDay를 찾았으므로 이제 7일씩 더하면서 범위 내 모든 날짜를 저장
		 */
		while (!date.isAfter(end)) {
			result.add(date);
			date = date.plusDays(7);
		}

		return result;
	}

}

