package com.popjub.store_service.domain.entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_store_category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE p_store_category SET deleted_at = NOW(), deleted_by = ? WHERE store_category_id = ?")
@Where(clause = "deleted_at IS NULL")
public class StoreCategory /*extends BaseEntity*/{

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

	public StoreCategory(Store store, Category category) {
		if (store == null || category == null) {
			throw new IllegalArgumentException("Store와 Category는 필수입니다.");
		}
		this.store = store;
		this.category = category;
	}

	// 카테고리 이름 조회
	public String getCategoryName() {
		return category.getName();
	}
}