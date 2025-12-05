package com.popjub.store_service.presentation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.popjub.store_service.application.dto.result.SearchStoreResult;
import com.popjub.store_service.domain.entity.StoreStatus;

public record SearchStoreResponse(
	String storeName,
	String categories,
	String address,
	BigDecimal latitude,
	BigDecimal longitude,
	LocalDate startDate,
	LocalDate endDate,
	List<SearchTimeRuleResponse> timeRules,
	BigDecimal ratingAvg,
	StoreStatus status,
	Boolean isFree,
	Integer price,
	String imageUrl
) {

	public static SearchStoreResponse from(SearchStoreResult result) {
		return new SearchStoreResponse(
			result.storeName(),
			String.join(", ", result.categories()), // ← 한줄로 변환
			result.address(),
			result.latitude(),
			result.longitude(),
			result.startDate(),
			result.endDate(),
			result.timeRules().stream()
				.map(SearchTimeRuleResponse::from)
				.toList(),
			result.ratingAvg(),
			result.status(),
			result.isFree(),
			result.price(),
			result.imageUrl()
		);
	}
}
