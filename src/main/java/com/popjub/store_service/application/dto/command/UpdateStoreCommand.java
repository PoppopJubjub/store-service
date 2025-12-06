package com.popjub.store_service.application.dto.command;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.popjub.store_service.domain.entity.StoreStatus;

public record UpdateStoreCommand(
	String storeName,
	String address,
	BigDecimal latitude,
	BigDecimal longitude,
	LocalDate startDate,
	LocalDate endDate,
	StoreStatus status,
	Integer price,
	List<Long> categoryIds,
	String imageUrl,
	String description
) {
}
