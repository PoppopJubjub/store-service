package com.popjub.store_service.application.service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.popjub.store_service.application.dto.command.CreateStoreCommand;
import com.popjub.store_service.application.dto.command.CreateTimeRuleCommand;
import com.popjub.store_service.application.dto.result.CreateStoreResult;
import com.popjub.store_service.domain.entity.Category;
import com.popjub.store_service.domain.entity.Store;
import com.popjub.store_service.domain.entity.StoreCategory;
import com.popjub.store_service.domain.entity.StoreTime;
import com.popjub.store_service.domain.repository.CategoryRepository;
import com.popjub.store_service.domain.repository.StoreCategoryRepository;
import com.popjub.store_service.domain.repository.StoreRepository;
import com.popjub.store_service.domain.repository.StoreTimeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

	private final StoreRepository storeRepository;
	private final CategoryRepository categoryRepository;
	private final StoreCategoryRepository storeCategoryRepository;
	private final StoreTimeRepository storeTimeRepository;

	@Transactional
	public CreateStoreResult createStore(
		CreateStoreCommand storeCommand,
		List<CreateTimeRuleCommand> timeRuleCommands,
		List<Long> categoryIds)
	{
		validateStore(storeCommand);
		validateAllDayCovered(timeRuleCommands);

		Store store = storeRepository.save(storeCommand.toEntity());

		// 4) 운영시간 생성 및 저장
		List<StoreTime> storeTimes =
			timeRuleCommands.stream()
				.flatMap(rule -> rule.createStoreTimes(
					store,
					storeCommand.startDate(),
					storeCommand.endDate()
				).stream())
				.toList();
		storeTimeRepository.saveAll(storeTimes);

		// 5) 카테고리 매핑 생성 및 저장
		List<StoreCategory> storeCategories = buildStoreCategories(store, categoryIds);
		storeCategoryRepository.saveAll(storeCategories);

		// 6) storeId만 반환
		return CreateStoreResult.from(store);
	}

	// ================== 검증 메서드 ==================
	// todo : customException처리
	private void validateStore(CreateStoreCommand cmd) {
		validateLocation(cmd.latitude(), cmd.longitude());
		validatePeriod(cmd.startDate(), cmd.endDate());
		validatePricePolicy(cmd.isFree(), cmd.price());
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

	private void validatePricePolicy(Boolean isFree, Integer price) {
		// 무료 스토어인데 price가 설정된 경우
		if (Boolean.TRUE.equals(isFree) && price != null) {
			throw new IllegalArgumentException("무료 스토어는 가격을 설정할 수 없습니다.");
		}

		// 유료 스토어인데 price가 없거나 음수인 경우
		if (!Boolean.TRUE.equals(isFree) && (price == null || price < 1)) {
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

	private List<StoreCategory> buildStoreCategories(Store store, List<Long> categoryIds) {
		List<Category> categories = categoryRepository.findAllById(categoryIds);

		if (categories.size() != categoryIds.size()) {
			throw new IllegalArgumentException("존재하지 않는 카테고리 ID가 포함되어 있습니다.");
		}

		List<StoreCategory> result = new ArrayList<>();
		for (Category category : categories) {
			result.add(StoreCategory.of(store, category));
		}
		return result;
	}
}
