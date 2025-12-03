package com.popjub.store_service.domain.entity;

import com.popjub.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_store_category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// @SQLDelete(sql = "UPDATE p_store_category SET deleted_at = NOW(), deleted_by = ? WHERE store_category_id = ?")
// @Where(clause = "deleted_at IS NULL")
public class StoreCategory /* extends BaseEntity */ {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long storeCategoryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	// ✅ Category 엔티티 참조
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	// 엔티티 내부용 생성자 (검증은 DTO/Service에서)
	private StoreCategory(Store store, Category category) {
		this.store = store;
		this.category = category;
	}

	// 정적 팩토리 메서드
	public static StoreCategory of(Store store, Category category) {
		return new StoreCategory(store, category);
	}
}
