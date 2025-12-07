package com.popjub.store_service.application.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.popjub.store_service.application.dto.command.CreateStoreCommand;
import com.popjub.store_service.application.dto.command.CreateTimeRuleCommand;
import com.popjub.store_service.application.dto.command.UpdateStoreCommand;
import com.popjub.store_service.application.dto.command.UpdateStoreTimeCommand;
import com.popjub.store_service.application.dto.result.CreateStoreResult;
import com.popjub.store_service.application.dto.result.SearchStoreResult;
import com.popjub.store_service.application.dto.result.UpdateStoreResult;
import com.popjub.store_service.application.dto.result.UpdateStoreTimeResult;
import com.popjub.store_service.application.validation.StoreValidator;
import com.popjub.store_service.domain.entity.Category;
import com.popjub.store_service.domain.entity.Store;
import com.popjub.store_service.domain.entity.StoreCategory;
import com.popjub.store_service.domain.entity.StoreTime;
import com.popjub.store_service.domain.repository.CategoryRepository;
import com.popjub.store_service.domain.repository.StoreCategoryRepository;
import com.popjub.store_service.domain.repository.StoreRepository;
import com.popjub.store_service.domain.repository.StoreTimeRepository;
import com.popjub.store_service.exception.StoreCustomException;
import com.popjub.store_service.exception.StoreErrorCode;

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
	private final TimeSlotService timeSlotService;

	@Transactional
	public CreateStoreResult createStore(
		CreateStoreCommand storeCommand,
		List<CreateTimeRuleCommand> timeRuleCommands,
		List<Long> categoryIds) {
		storeValidator.validateCreateStore(storeCommand, timeRuleCommands, categoryIds);

		Store store = storeRepository.save(storeCommand.toEntity());

		List<StoreTime> storeTimes =
			timeRuleCommands.stream()
				.flatMap(rule -> rule.createStoreTimes(
					store,
					storeCommand.startDate(),
					storeCommand.endDate()
				).stream())
				.toList();
		storeTimeRepository.saveAll(storeTimes);

		List<StoreCategory> storeCategories = buildStoreCategories(store, categoryIds);
		storeCategoryRepository.saveAll(storeCategories);

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

	public Page<SearchStoreResult> searchStore(Pageable pageable) {
		Page<Store> storePage = storeRepository.findAll(pageable);

		return storePage.map(store -> {
			List<String> categories = storeCategoryRepository.findCategoryNamesByStore(store);
			List<StoreTime> storeTimes = storeTimeRepository.findAllByStore(store);
			return SearchStoreResult.from(store, categories, storeTimes);
		});
	}

	public SearchStoreResult searchStoreDetail(UUID storeId) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_STORE));
		List<String> categories = storeCategoryRepository.findCategoryNamesByStore(store);
		List<StoreTime> storeTimes = storeTimeRepository.findAllByStore(store);
		return SearchStoreResult.from(store, categories, storeTimes);
	}

	@Transactional
	public UpdateStoreResult updateStore(UUID storeId, UpdateStoreCommand command) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_STORE));

		storeValidator.validateUpdateStore(command);

		store.updateStore(
			command.storeName(),
			command.address(),
			command.latitude(),
			command.longitude(),
			command.startDate(),
			command.endDate(),
			command.status(),
			command.price(),
			command.imageUrl(),
			command.description()
		);
		List<String> categoryNames = storeCategoryRepository.findCategoryNamesByStore(store);
		return UpdateStoreResult.from(store, categoryNames);
	}

	@Transactional
	public UpdateStoreTimeResult updateStoreTime(UUID storeId, LocalDate date, UpdateStoreTimeCommand command) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_STORE));
		StoreTime storeTime = storeTimeRepository.findByStoreAndDate(store, date)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_STORE_TIME));

		storeValidator.validateUpdateStoreTime(
			command.startTime(),
			command.endTime()
		);

		storeTime.updateStoreTime(
			command.startTime(),
			command.endTime()
		);

		timeSlotService.RegenerateTimeSlots(store, date);
		return  UpdateStoreTimeResult.from(storeTime);
	}
}
