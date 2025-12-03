package com.popjub.store_service.domain.entity;

import com.popjub.common.entity.BaseEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//todo
public class Category extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long categoryId;

	@Column(name = "category_name", nullable = false, length = 50, unique = true)
	private String name;

	// 생성자: 엔티티 내부에서만 사용
	public Category(String name) {
		this.name = name;
	}

	// 정적 팩토리 메서드
	public static Category of(String name) {
		return new Category(name);
	}

	// 엔티티 수정 메서드 (검증은 DTO/서비스에서)
	public void updateName(String newName) {
		this.name = newName;
	}
}
