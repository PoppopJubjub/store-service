package com.popjub.storeservice.application.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.popjub.storeservice.application.dto.command.CreateStoreCommand;
import com.popjub.storeservice.application.dto.command.CreateTimeRuleCommand;
import com.popjub.storeservice.application.dto.command.UpdateRatingCommand;
import com.popjub.storeservice.application.dto.command.UpdateStoreCommand;
import com.popjub.storeservice.application.dto.command.UpdateStoreTimeCommand;
import com.popjub.storeservice.application.dto.result.CreateStoreResult;
import com.popjub.storeservice.application.dto.result.SearchStoreResult;
import com.popjub.storeservice.application.dto.result.UpdateStoreResult;
import com.popjub.storeservice.application.dto.result.UpdateStoreTimeResult;
import com.popjub.storeservice.application.validation.StoreValidator;
import com.popjub.storeservice.domain.entity.Category;
import com.popjub.storeservice.domain.entity.Store;
import com.popjub.storeservice.domain.entity.StoreCategory;
import com.popjub.storeservice.domain.entity.StoreTime;
import com.popjub.storeservice.domain.entity.TimeSlot;
import com.popjub.storeservice.domain.repository.CategoryRepository;
import com.popjub.storeservice.domain.repository.StoreCategoryRepository;
import com.popjub.storeservice.domain.repository.StoreRepository;
import com.popjub.storeservice.domain.repository.StoreTimeRepository;
import com.popjub.storeservice.domain.repository.TimeSlotRepository;
import com.popjub.storeservice.exception.StoreCustomException;
import com.popjub.storeservice.exception.StoreErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

	private final StoreRepository storeRepository;
	private final CategoryRepository categoryRepository;
	private final StoreCategoryRepository storeCategoryRepository;
	private final StoreTimeRepository storeTimeRepository;
	private final TimeSlotRepository timeSlotRepository;
	private final StoreValidator storeValidator;
	private final TimeSlotService timeSlotService;

	@Transactional
	public CreateStoreResult createStore(
		CreateStoreCommand storeCommand,
		List<CreateTimeRuleCommand> timeRuleCommands,
		List<Long> categoryIds,
		Long currentUserId) {
		storeValidator.validateCreateStore(storeCommand, timeRuleCommands, categoryIds);

		Store store = storeRepository.save(storeCommand.toEntity(currentUserId));

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
	public UpdateStoreResult updateStore(UUID storeId, UpdateStoreCommand command, Long currentUserId) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_STORE));

		if(store.isNotManagedBy(currentUserId)) {
			throw new StoreCustomException(StoreErrorCode.FORBIDDEN_STORE_ACCESS);
		}
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


	@Transactional
	public void deleteStoreCategory(UUID storeId, Long CategoryId) {
		//todo Admin, StoreManger(본인 가게)만 가능하게
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_STORE));

		Category category = categoryRepository.findById(CategoryId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_CATEGORY));

		StoreCategory storeCategory = storeCategoryRepository.findByStoreAndCategory(store, category)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_CATEGORY));

		Long deletedBy = 1L;

		storeCategory.softDelete(deletedBy);
	}

	@Transactional
	public void deleteStore(UUID storeId) {
		//todo Admin, StoreManager(본인 가게)만 가능하게

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_STORE));

		Long deletedBy = 1L;

		// 연관 StoreTime, Timeslot , storeCategory 조회해서 softDelete
		List<StoreTime> storeTimes = storeTimeRepository.findAllByStore(store);

		List<TimeSlot> timeSlots = timeSlotRepository.findAllByStore(store);

		for(TimeSlot timeSlot : timeSlots){
			timeSlot.softDelete(deletedBy);
		}
		for(StoreTime storeTime : storeTimes){
			storeTime.softDelete(deletedBy);
		}
		List<StoreCategory> categories = storeCategoryRepository.findAllByStore(store);
		for(StoreCategory storeCategory : categories){
			storeCategory.softDelete(deletedBy);
		}

		store.softDelete(deletedBy);
	}

	@Transactional
	public void increaseRating(UpdateRatingCommand command) {
		Store store = storeRepository.findById(command.storeId())
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_STORE));
		store.increaseRating(command.rating());
	}
	@Transactional
	public void decreaseRating(UpdateRatingCommand command) {
		Store store = storeRepository.findById(command.storeId())
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_STORE));
		store.decreaseRating(command.rating());
	}
}
