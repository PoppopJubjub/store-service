package com.popjub.store_service.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.popjub.store_service.domain.entity.Store;
import com.popjub.store_service.domain.entity.StoreCategory;

public interface StoreCategoryJpaRepository extends JpaRepository<StoreCategory,Long> {

	@Query("select sc.category.categoryName from StoreCategory sc where sc.store = :store")
	List<String> findByCategoryNameByStore(@Param("store") Store store);
}
