package com.popjub.storeservice.presentation.dto.request;

import java.util.UUID;

import com.popjub.storeservice.application.dto.command.UpdateRatingCommand;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record UpdateRatingRequest(
	UUID storeId,
	@Min(value = 1, message = "평점은 1~5 사이여야 합니다.")
	@Max(value = 5, message = "평점은 1~5 사이여야 합니다.")
	Integer rating
) {
	public UpdateRatingCommand toCommand(UpdateRatingRequest request) {
		return new UpdateRatingCommand(
			storeId,
			rating
		);
	}
}