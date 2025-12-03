package com.popjub.store_service.application.dto.result;

import java.util.UUID;

import com.popjub.store_service.domain.entity.Store;

public record CreateStoreResult(
	UUID storeId
) {
	public static CreateStoreResult from(Store store) {
		return new CreateStoreResult(store.getStoreId());
	}
}
