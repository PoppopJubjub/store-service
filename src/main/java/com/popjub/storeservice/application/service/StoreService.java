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
import com.popjub.storeservice.application.validation.TimeSlotValidator;
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
	private final TimeSlotValidator timeSlotValidator;

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
		Store store = getStore(storeId);
		List<String> categories = storeCategoryRepository.findCategoryNamesByStoreId(storeId);
		List<StoreTime> storeTimes = storeTimeRepository.findAllByStoreId(storeId);
		return SearchStoreResult.from(store, categories, storeTimes);
	}

	@Transactional
	public UpdateStoreResult updateStore(UUID storeId, UpdateStoreCommand command, Long currentUserId, List<String> role) {
		Store store = getStore(storeId);

		storeValidator.validateManagerOrAdmin(store, currentUserId, role);

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
	public UpdateStoreTimeResult updateStoreTime(UUID storeId, LocalDate date, UpdateStoreTimeCommand command, Long currentUserId, List<String> role) {
		Store store = getStore(storeId);
		StoreTime storeTime = getStoreTime(store, date);

		storeValidator.validateManagerOrAdmin(store, currentUserId, role);

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
	public void deleteStoreCategory(UUID storeId, Long CategoryId, Long currentUserId, List<String> role) {
		Store store = getStore(storeId);

		Category category = getCategory(CategoryId);

		StoreCategory storeCategory = storeCategoryRepository.findByStoreAndCategory(store, category)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_CATEGORY));

		storeValidator.validateManagerOrAdmin(store, currentUserId, role);

		storeCategory.softDelete(currentUserId);
	}

	@Transactional
	public void deleteStore(UUID storeId, Long currentUserId, List<String> role) {

		Store store = getStore(storeId);

		storeValidator.validateManagerOrAdmin(store, currentUserId, role);
		// 연관 StoreTime, Timeslot , storeCategory 조회해서 softDelete
		List<StoreTime> storeTimes = storeTimeRepository.findAllByStore(store);

		List<TimeSlot> timeSlots = timeSlotRepository.findAllByStore(store);

		for(TimeSlot timeSlot : timeSlots){
			timeSlot.softDelete(currentUserId);
		}
		for(StoreTime storeTime : storeTimes){
			storeTime.softDelete(currentUserId);
		}
		List<StoreCategory> categories = storeCategoryRepository.findAllByStore(store);
		for(StoreCategory storeCategory : categories){
			storeCategory.softDelete(currentUserId);
		}

		store.softDelete(currentUserId);
	}

	@Transactional
	public void increaseRating(UpdateRatingCommand command) {
		Store store = getStore(command.storeId());
		store.increaseRating(command.rating());
	}
	@Transactional
	public void decreaseRating(UpdateRatingCommand command) {
		Store store = getStore(command.storeId());
		store.decreaseRating(command.rating());
	}


	public boolean validateCheckin(UUID storeId, UUID timeSlotId, Long currentUserId) {
		try {
			Store store = getStore(storeId);
			TimeSlot timeSlot = getTimeSlot(timeSlotId);
			if(store.isNotManagedBy(currentUserId)){
				throw new StoreCustomException(StoreErrorCode.FORBIDDEN_STORE_ACCESS);
			}
			timeSlotValidator.validateCheckin(timeSlot, store);

			return true;
		}catch (StoreCustomException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return false;
		}
	}

	public boolean existsStore(UUID storeId) {
		return storeRepository.existsByStoreId(storeId);
	}

	@Transactional
	public void openStores(LocalDate today) {
		storeRepository.openStores(today);
	}

	@Transactional
	public void closeStores(LocalDate today) {
		storeRepository.closeStores(today);
	}

	private Store getStore(UUID storeId) {
		return storeRepository.findById(storeId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_STORE));
	}

	private Category getCategory(Long categoryId) {
		return categoryRepository.findById(categoryId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_CATEGORY));
	}

	private StoreTime getStoreTime(Store store, LocalDate date) {
		return storeTimeRepository.findByStoreAndDate(store, date)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_STORE_TIME));
	}

	private TimeSlot getTimeSlot(UUID timeSlotId) {
		return timeSlotRepository.findById(timeSlotId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NOT_FOUND_TIME_SLOT));
	}
}
