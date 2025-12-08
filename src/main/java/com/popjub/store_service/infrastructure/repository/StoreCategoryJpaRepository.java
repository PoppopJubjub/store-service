package com.popjub.store_service.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.popjub.store_service.domain.entity.Category;
import com.popjub.store_service.domain.entity.Store;
import com.popjub.store_service.domain.entity.StoreCategory;

public interface StoreCategoryJpaRepository extends JpaRepository<StoreCategory,Long> {

	@Query("select sc.category.categoryName from StoreCategory sc where sc.store = :store")
	List<String> findByCategoryNameByStore(@Param("store") Store store);

	// 카테고리를 기준으로 StoreCategory가 존재하는지 여부 확인
	boolean existsByCategory(Category category);

	List<StoreCategory> findAllByCategory(Category category);

	Optional<StoreCategory> findByStoreAndCategory(Store store, Category category);

}
