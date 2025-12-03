package com.popjub.store_service.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_store")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE p_store SET deleted_at = NOW(), deleted_by = ? WHERE store_id = ?")
@Where(clause = "deleted_at IS NULL")
public class Store /*extends BaseEntity*/ {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID storeId;

	@Column(name = "store_manager_id", nullable = false)
	private Long storeManagerId;

	@Column(name = "store_name", nullable = false, length = 100)
	private String name;

	@Column(name = "address", nullable = false, length = 50)
	private String address;

	@Column(name = "latitude", nullable = false, precision = 9, scale = 6)
	private BigDecimal latitude;

	@Column(name = "longitude", nullable = false, precision = 9, scale = 6)
	private BigDecimal longitude;

	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@Column(name = "end_date", nullable = false)
	private LocalDate endDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private StoreStatus status = StoreStatus.UPCOMING;

	@Column(name = "rating_avg", nullable = false, precision = 2, scale = 1)
	private BigDecimal ratingAvg = BigDecimal.ZERO;

	@Column(name = "total_review", nullable = false)
	private Integer totalReview = 0;

	@Column(name = "is_free", nullable = false)
	private Boolean isFree;

	@Column(name = "price")
	private Integer price;

	// ✅ 변경: 양방향 매핑 제거 (timeslots 컬렉션 삭제)

	@Builder
	public Store(Long storeManagerId, String name, String address, BigDecimal latitude, BigDecimal longitude,
		LocalDate startDate, LocalDate endDate, Boolean isFree, Integer price) {

		this.storeManagerId = storeManagerId;
		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.startDate = startDate;
		this.endDate = endDate;
		updatePricing(isFree, price);
	}

	/* ================== 도메인 검증 ================== */

	@PrePersist
	@PreUpdate
	private void validateBeforeSave() {
		validate();
	}

	private void validate() {
		validateLocation();
		validatePeriod();
		validatePrice();
	}

	private void validatePeriod() {
		if (endDate.isBefore(startDate)) {
			throw new IllegalArgumentException("운영 종료일은 시작일보다 빠를 수 없습니다.");
		}
	}

	private void validateLocation() {
		if (latitude.compareTo(BigDecimal.valueOf(-90)) < 0 || latitude.compareTo(BigDecimal.valueOf(90)) > 0) {
			throw new IllegalArgumentException("위도는 -90 ~ 90 범위여야 합니다.");
		}

		if (longitude.compareTo(BigDecimal.valueOf(-180)) < 0 || longitude.compareTo(BigDecimal.valueOf(180)) > 0) {
			throw new IllegalArgumentException("경도는 -180 ~ 180 범위여야 합니다.");
		}
	}

	private void validatePrice() {
		if (!Boolean.TRUE.equals(isFree) && (price == null || price < 0)) {
			throw new IllegalArgumentException("유료 스토어는 0 이상 가격이 필요합니다.");
		}
		if (Boolean.TRUE.equals(isFree) && price != null) {
			throw new IllegalArgumentException("무료 스토어는 가격을 설정할 수 없습니다.");
		}
	}

	/* ================== 도메인 수정 메서드 ================== */

	public void updateStoreInfo(String name, String address, BigDecimal latitude, BigDecimal longitude,
		LocalDate startDate, LocalDate endDate) {

		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.startDate = startDate;
		this.endDate = endDate;

		validateLocation();
		validatePeriod();
	}

	public void updateReviewStats(BigDecimal newAvg, int newTotal) {
		this.ratingAvg = newAvg;
		this.totalReview = newTotal;
	}

	public void updatePricing(Boolean isFree, Integer price) {
		this.isFree = isFree;
		this.price = Boolean.TRUE.equals(isFree) ? null : price;
		validatePrice();
	}
	/* ================== 편의 메서드 ================== */
}