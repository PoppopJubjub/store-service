package com.popjub.store_service.application.dto.command;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.popjub.store_service.domain.entity.Store;
import com.popjub.store_service.domain.entity.StoreTime;

public record CreateTimeRuleCommand(
	List<String> daysOfWeek,
	LocalTime startTime,
	LocalTime endTime
) {

	public List<StoreTime> createStoreTimes(Store store, LocalDate start, LocalDate end) {
		List<StoreTime> result = new ArrayList<>();

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

	private List<LocalDate> resolveDatesByDayOfWeek(LocalDate start, LocalDate end, DayOfWeek targetDay) {
		List<LocalDate> result = new ArrayList<>();

		LocalDate date = start;

		while (date.getDayOfWeek() != targetDay) {
			date = date.plusDays(1);
			if (date.isAfter(end))
				return result;
		}

		while (!date.isAfter(end)) {
			result.add(date);
			date = date.plusDays(7);
		}

		return result;
	}

}

