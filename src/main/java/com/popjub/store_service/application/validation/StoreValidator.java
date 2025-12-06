package com.popjub.store_service.application.validation;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.popjub.store_service.application.dto.command.CreateStoreCommand;
import com.popjub.store_service.application.dto.command.CreateTimeRuleCommand;
import com.popjub.store_service.application.dto.command.UpdateStoreCommand;
import com.popjub.store_service.domain.entity.Category;
import com.popjub.store_service.domain.repository.CategoryRepository;
import com.popjub.store_service.exception.StoreCustomException;
import com.popjub.store_service.exception.StoreErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class StoreValidator {

	private final CategoryRepository categoryRepository;

	public void validateCreateStore(
		CreateStoreCommand storeCommand,
		List<CreateTimeRuleCommand> timeRuleCommands,
		List<Long> categoryIds
	) {
		validateLocation(storeCommand.latitude(), storeCommand.longitude());
		validatePeriod(storeCommand.startDate(), storeCommand.endDate());
		validatePricePolicy(storeCommand.price());
		validateAllDayCovered(timeRuleCommands);
		validateCategory(categoryIds);
	}

	public void validateUpdateStore(UpdateStoreCommand storeCommand){
		if(storeCommand.latitude() != null || storeCommand.longitude() != null) {
			if(storeCommand.latitude() != null && storeCommand.longitude() != null) {
				validateLocation(storeCommand.latitude(), storeCommand.longitude());
			}
		}

		if(storeCommand.startDate() != null || storeCommand.endDate() != null) {
			validatePeriod(storeCommand.startDate(), storeCommand.endDate());
		}
		validatePricePolicy(storeCommand.price());
		if(storeCommand.categoryIds() != null) {
			validateCategory(storeCommand.categoryIds());
		}
	}

	// ================== 내부 검증 메서드들 ==================

	private void validateCategory(List<Long> categoryIds){
		List<Category> categories = categoryRepository.findAllById(categoryIds);

		if (categories.size() != categoryIds.size()) {
			throw new StoreCustomException(StoreErrorCode.INVALID_CATEGORY_IDS);
		}

	}
	private void validateLocation(BigDecimal latitude, BigDecimal longitude) {
		if (latitude.compareTo(BigDecimal.valueOf(-90)) < 0 ||
			latitude.compareTo(BigDecimal.valueOf(90)) > 0) {
			throw new StoreCustomException(StoreErrorCode.INVALID_LATITUDE);
		}

		if (longitude.compareTo(BigDecimal.valueOf(-180)) < 0 ||
			longitude.compareTo(BigDecimal.valueOf(180)) > 0) {
			throw new StoreCustomException(StoreErrorCode.INVALID_LONGITUDE);
		}
	}

	private void validatePeriod(LocalDate startDate, LocalDate endDate) {
		if (endDate.isBefore(startDate)) {
			throw new StoreCustomException(StoreErrorCode.INVALID_OPERATION_PERIOD);
		}
	}

	private void validatePricePolicy(Integer price) {
		if (price == null) {
			return;
		}

		if (price < 1) {
			throw new StoreCustomException(StoreErrorCode.INVALID_PRICE_POLICY);
		}
	}

	private void validateAllDayCovered(List<CreateTimeRuleCommand> rules) {
		EnumSet<DayOfWeek> days = EnumSet.noneOf(DayOfWeek.class);

		for (CreateTimeRuleCommand rule : rules) {
			for (String day : rule.daysOfWeek()) {
				DayOfWeek dayOfWeek = DayOfWeek.valueOf(day);
				days.add(dayOfWeek);
			}
		}

		Set<DayOfWeek> required = EnumSet.allOf(DayOfWeek.class);
		if (!days.containsAll(required)) {
			throw new StoreCustomException(StoreErrorCode.INVALID_TIME_RULE_DAYS);
		}
	}
}
