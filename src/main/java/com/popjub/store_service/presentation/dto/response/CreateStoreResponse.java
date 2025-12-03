package com.popjub.store_service.presentation.dto.response;

import java.util.UUID;

import com.popjub.store_service.application.dto.result.CreateStoreResult;

public record CreateStoreResponse(
	UUID storeId
) {
	public static CreateStoreResponse from(CreateStoreResult result) {
		return new CreateStoreResponse(result.storeId());
	}
}
