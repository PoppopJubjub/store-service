package com.popjub.store_service.application.dto.result;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.popjub.store_service.domain.entity.Store;
import com.popjub.store_service.domain.entity.StoreStatus;
import com.popjub.store_service.domain.entity.StoreTime;

public record SearchStoreResult(
	String storeName,
	String categories,
	String address,
	BigDecimal latitude,
	BigDecimal longitude,
	LocalDate startDate,
	LocalDate endDate,
	List<SearchTimeRuleResult> timeRules,
	BigDecimal ratingAvg,
	StoreStatus status,
	Boolean isFree,
	Integer price,
	String imageUrl
) {
	public static SearchStoreResult from(
		Store store,
		List<String> categoryList,
		List<StoreTime> storeTimes
	) {
		//category 한줄로 표현
		String categories = String.join(", ", categoryList);

		//StoreTime들을 (startTime, endTime)이 동일한 것끼리 그룹핑
		Map<TimeRange, List<DayOfWeek>> grouped = storeTimes.stream()
			.collect(Collectors.groupingBy(
				(StoreTime st) -> new TimeRange(st.getStartTime(), st.getEndTime()),
				Collectors.mapping(StoreTime::getDayOfWeek, Collectors.toList())
			));
		/**
		 * 그룹핑된 결과를 SearchTimeRuleResult 리스트로 변환
		 * ex) key = timeRange(09:00 , 18:00) value = [MONDAY,TUESDAY] -> key에 해당하는 요일 목록
		 * → SearchTimeRuleResult(daysOfWeek=[MONDAY, TUESDAY], startTime=09:00, endTime=18:00)
 		 */
		List<SearchTimeRuleResult> timeRuleResults = grouped.entrySet().stream()
			.map(entry -> {
				//"MONDAY","TUESDAY"등 요일 중복 제거, String형태로 1줄로 반환 ex) [MONDAY , TUESDAY]
				String daysOfWeek = entry.getValue().stream()
					.distinct()
					.sorted()
					.map(DayOfWeek::name)
					.collect(Collectors.joining(", "));
				
				return new SearchTimeRuleResult(
					daysOfWeek,
					entry.getKey().start(),
					entry.getKey().end()
				);
			})
			.toList();

		return new SearchStoreResult(
			store.getName(),
			categories,
			store.getAddress(),
			store.getLatitude(),
			store.getLongitude(),
			store.getStartDate(),
			store.getEndDate(),
			timeRuleResults,
			store.getRatingAvg(),
			store.getStatus(),
			store.getIsFree(),
			store.getPrice(),
			store.getImageUrl()
		);
	}
	/**
	 *	동일한 startTime + endTime 조합을 하나로 묶기 위한 key
	 *	ex) MONDAY : 09:00 ~ 18:00
	 *		TUESDAY : 09:00 ~ 18:00
	 *		TimeRange(09:00 , 18:00) -> ["MONDAY","TUESDAY"] + 09:00 ~ 18:00 형태의 하나의 타임룰 생성
	 */
	private static record TimeRange(LocalTime start, LocalTime end) {}
}
