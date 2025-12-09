package com.popjub.storeservice.application.dto.command;

import java.util.UUID;

import com.popjub.storeservice.presentation.dto.request.UpdateRatingRequest;

public record UpdateRatingCommand(
	UUID storeId,
	Integer rating
) {
	public static UpdateRatingCommand from(UpdateRatingRequest request) {
		return new UpdateRatingCommand(
			request.storeId(),
			request.rating()
		);
	}
}