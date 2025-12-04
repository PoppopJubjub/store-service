package com.popjub.store_service.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.popjub.store_service.application.dto.command.CreateStoreCommand;
import com.popjub.store_service.application.dto.command.CreateTimeRuleCommand;
import com.popjub.store_service.application.dto.result.CreateStoreResult;
import com.popjub.store_service.application.validation.StoreValidator;
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
	private final StoreValidator storeValidator;

	@Transactional
	public CreateStoreResult createStore(
		CreateStoreCommand storeCommand,
		List<CreateTimeRuleCommand> timeRuleCommands,
		List<Long> categoryIds)
	{
		storeValidator.validateCreateStore(storeCommand, timeRuleCommands,categoryIds);

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

	private List<StoreCategory> buildStoreCategories(Store store, List<Long> categoryIds) {
		List<Category> categories = categoryRepository.findAllById(categoryIds);
		List<StoreCategory> result = new ArrayList<>();
		for (Category category : categories) {
			result.add(StoreCategory.of(store, category));
		}
		return result;
	}
}
