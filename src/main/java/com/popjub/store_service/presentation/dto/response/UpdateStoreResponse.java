package com.popjub.store_service.presentation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.popjub.store_service.application.dto.result.UpdateStoreResult;

public record UpdateStoreResponse(
	UUID storeId,
	String storeName,
	String address,
	BigDecimal latitude,
	BigDecimal longitude,
	LocalDate startDate,
	LocalDate endDate,
	String status,
	boolean isFree,
	Integer price,
	List<String> category,
	@JsonProperty("image_url")
	String imageUrl,
	String description
) {

	public static UpdateStoreResponse from(UpdateStoreResult result) {
		return new UpdateStoreResponse(
			result.storeId(),
			result.storeName(),
			result.address(),
			result.latitude(),
			result.longitude(),
			result.startDate(),
			result.endDate(),
			result.status().name(),
			result.isFree(),
			result.price(),
			result.categories(),
			result.imageUrl(),
			result.description()
		);
	}
}
