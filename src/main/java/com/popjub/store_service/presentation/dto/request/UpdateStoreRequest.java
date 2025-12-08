package com.popjub.store_service.presentation.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.popjub.store_service.application.dto.command.UpdateStoreCommand;
import com.popjub.store_service.domain.entity.StoreStatus;

import jakarta.validation.constraints.Size;

public record UpdateStoreRequest(

	@Size(max = 100, message = "스토어 이름은 최대 100자까지 가능합니다.")
	String storeName,
	@Size(max = 255, message = "주소는 최대 255자까지 가능합니다.")
	String address,
	BigDecimal latitude,
	BigDecimal longitude,
	LocalDate startDate,
	LocalDate endDate,
	StoreStatus status,
	Integer price,
	List<Long> categoryIds,
	@JsonProperty("image_url")
	String imageUrl,
	@Size(max = 500, message = "소개글은 500자까지 가능합니다.")
	String description
) {
	public UpdateStoreCommand toCommand() {
		return new UpdateStoreCommand(
			storeName,
			address,
			latitude,
			longitude,
			startDate,
			endDate,
			status,
			price,
			categoryIds,
			imageUrl,
			description
		);
	}
}
