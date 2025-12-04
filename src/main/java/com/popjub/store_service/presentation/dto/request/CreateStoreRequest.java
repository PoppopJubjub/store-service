package com.popjub.store_service.presentation.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.popjub.store_service.application.dto.command.CreateStoreCommand;
import com.popjub.store_service.application.dto.command.CreateTimeRuleCommand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateStoreRequest(

	//image

	@NotNull(message = "스토어 매니저 ID는 필수입니다.")
	Long storeManagerId,

	@NotBlank(message = "스토어 이름은 필수입니다.")
	String storeName,

	@NotBlank(message = "주소는 필수입니다.")
	String address,

	@NotNull(message = "위도는 필수 입력값입니다.")
	BigDecimal latitude,

	@NotNull(message = "경도는 필수 입력값입니다.")
	BigDecimal longitude,

	@NotNull(message = "운영 시작일은 필수입니다.")
	LocalDate startDate,

	@NotNull(message = "운영 종료일은 필수입니다.")
	LocalDate endDate,

	@NotNull(message = "무료/유료 여부는 필수입니다.")
	Boolean isFree,


	Integer price,

	@NotEmpty(message = "운영 규칙(timeRules)은 필수입니다.")
	List<CreateTimeRuleRequest> timeRules,

	@NotEmpty(message = "카테고리 ID 목록은 필수입니다.")
	List<Long> categoryIds
) {

	public CreateStoreCommand toStoreCommand() {
		return new CreateStoreCommand(
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

	public List<CreateTimeRuleCommand> toTimeRulesCommand() {
		return timeRules.stream()
			.map(rule -> new CreateTimeRuleCommand(
				rule.daysOfWeek(),
				rule.startTime(),
				rule.endTime()
			))
			.toList();
	}
	public List<Long> toCategoryIds() {
		return categoryIds;
	}
}
