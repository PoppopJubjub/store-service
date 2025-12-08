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

	// ✅ 해당 스토어에 연결된 "삭제되지 않은" 카테고리 이름들만 조회
	@Query("""
		select sc.category.categoryName 
		from StoreCategory sc 
		where sc.store = :store 
		  and sc.deletedAt is null 
		  and sc.category.deletedAt is null
	""")
	List<String> findActiveCategoryNamesByStore(@Param("store") Store store);

	// ✅ 삭제되지 않은 StoreCategory 목록 반환 (카테고리 강제 삭제 시 soft delete용)
	List<StoreCategory> findAllByCategoryAndDeletedAtIsNull(Category category);

	// ✅ 삭제되지 않은 StoreCategory 한 건 조회 (스토어에서 카테고리 제거 시 사용)
	Optional<StoreCategory> findByStoreAndCategoryAndDeletedAtIsNull(Store store, Category category);
}
