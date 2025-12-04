package com.popjub.store_service.application.dto.command;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.popjub.store_service.domain.entity.Store;

public record CreateStoreCommand(
	Long storeManagerId,
	String storeName,
	String address,
	BigDecimal latitude,
	BigDecimal longitude,
	LocalDate startDate,
	LocalDate endDate,
	Integer price // null이면 무료, >=1이면 유료
) {

	public Store toEntity() {
		if (price == null) {
			// 무료 스토어
			return Store.createFreeStore(
				storeManagerId,
				storeName,
				address,
				latitude,
				longitude,
				startDate,
				endDate
			);
		}

		// 유료 스토어
		return Store.createPaidStore(
			storeManagerId,
			storeName,
			address,
			latitude,
			longitude,
			startDate,
			endDate,
			price
		);
	}
}
