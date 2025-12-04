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
import com.popjub.store_service.domain.entity.Category;
import com.popjub.store_service.domain.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class StoreValidator {

	private final CategoryRepository categoryRepository;

	//todo : CustomException 적용
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

	// ================== 내부 검증 메서드들 ==================

	private void validateCategory(List<Long> categoryIds){
		List<Category> categories = categoryRepository.findAllById(categoryIds);

		if (categories.size() != categoryIds.size()) {
			throw new IllegalArgumentException("존재하지 않는 카테고리 ID가 포함되어 있습니다.");
		}

	}
	private void validateLocation(BigDecimal latitude, BigDecimal longitude) {
		if (latitude.compareTo(BigDecimal.valueOf(-90)) < 0 ||
			latitude.compareTo(BigDecimal.valueOf(90)) > 0) {
			throw new IllegalArgumentException("위도는 -90 ~ 90 범위여야 합니다.");
		}

		if (longitude.compareTo(BigDecimal.valueOf(-180)) < 0 ||
			longitude.compareTo(BigDecimal.valueOf(180)) > 0) {
			throw new IllegalArgumentException("경도는 -180 ~ 180 범위여야 합니다.");
		}
	}

	private void validatePeriod(LocalDate startDate, LocalDate endDate) {
		if (endDate.isBefore(startDate)) {
			throw new IllegalArgumentException("운영 종료일은 시작일보다 빠를 수 없습니다.");
		}
	}

	private void validatePricePolicy(Integer price) {
		if (price == null) {
			return;
		}

		if (price < 1) {
			throw new IllegalArgumentException("유료 스토어는 1 이상 가격이 필요합니다.");
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
			throw new IllegalArgumentException("모든 요일(MONDAY~SUNDAY)의 운영시간이 필요합니다.");
		}
	}
}
