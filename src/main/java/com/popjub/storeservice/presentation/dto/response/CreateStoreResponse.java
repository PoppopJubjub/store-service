package com.popjub.storeservice.presentation.dto.response;

import java.util.UUID;

import com.popjub.storeservice.application.dto.result.CreateStoreResult;

public record CreateStoreResponse(
	UUID storeId
) {
	public static CreateStoreResponse from(CreateStoreResult result) {
		return new CreateStoreResponse(result.storeId());
	}
}
