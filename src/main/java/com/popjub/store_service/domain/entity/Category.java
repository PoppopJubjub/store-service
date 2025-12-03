package com.popjub.store_service.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category /*extends BaseEntity*/{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long categoryId;

	@Column(name = "category_name", nullable = false, length = 50, unique = true)
	private String name;

	public Category(String name) {
		this.name = name;
	}

	public void updateName(String newName) {
		validateName(newName);
		this.name = newName;
	}

	/* ================== 검증 ================== */

	@PrePersist
	@PreUpdate // DB 저장/수정 전에 검증
	private void validateBeforeSave() {
		validate();
	}

	private void validate() {
		validateName(this.name);
	}

	private void validateName(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("카테고리 이름은 Null 일 수 없습니다.");
		}
		if (name.length() > 50) {
			throw new IllegalArgumentException("카테고리 이름은 최대 50자입니다.");
		}
	}
}
