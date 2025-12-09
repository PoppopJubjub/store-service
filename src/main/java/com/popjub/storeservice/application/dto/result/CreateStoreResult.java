package com.popjub.storeservice.application.dto.result;

import java.util.UUID;

import com.popjub.storeservice.domain.entity.Store;

public record CreateStoreResult(
	UUID storeId
) {
	public static CreateStoreResult from(Store store) {
		return new CreateStoreResult(store.getStoreId());
	}
}
