package com.popjub.storeservice.application.dto.result;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.popjub.storeservice.domain.entity.Store;
import com.popjub.storeservice.domain.entity.StoreStatus;

public record UpdateStoreResult(
	UUID storeId,
	String storeName,
	String address,
	BigDecimal latitude,
	BigDecimal longitude,
	LocalDate startDate,
	LocalDate endDate,
	StoreStatus status,
	boolean isFree,
	Integer price,
	List<String> categories,
	String imageUrl,
	String description
) {

	public static UpdateStoreResult from(Store store, List<String> categoryNames) {

		return new UpdateStoreResult(
			store.getStoreId(),
			store.getName(),
			store.getAddress(),
			store.getLatitude(),
			store.getLongitude(),
			store.getStartDate(),
			store.getEndDate(),
			store.getStatus(),
			store.getIsFree(),
			store.getPrice(),
			categoryNames,
			store.getImageUrl(),
			store.getDescription()
		);
	}
}
