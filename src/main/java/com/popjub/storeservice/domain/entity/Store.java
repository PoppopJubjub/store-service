package com.popjub.storeservice.domain.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

import com.popjub.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_store")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseEntity {

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
	private StoreStatus status;

	@Column(name = "rating_avg", nullable = false, precision = 2, scale = 1)
	private BigDecimal ratingAvg;

	@Column(name = "total_review", nullable = false)
	private Integer totalReview;

	@Column(name = "is_free", nullable = false)
	private Boolean isFree;

	@Column(name = "price")
	private Integer price;

	@Column(name = "image_url", length = 500, nullable = true)
	private String imageUrl;

	@Column(name = "description", length = 500, nullable = true)
	private String description;

	/* ================== 공통 private 생성자 ================== */
	@Builder(access = AccessLevel.PRIVATE)
	private Store(Long storeManagerId,
		String name,
		String address,
		BigDecimal latitude,
		BigDecimal longitude,
		LocalDate startDate,
		LocalDate endDate,
		Boolean isFree,
		Integer price) {

		this.storeManagerId = storeManagerId;
		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.startDate = startDate;
		this.endDate = endDate;

		this.isFree = isFree;
		this.price = price;

		// 기본값
		this.status = StoreStatus.UPCOMING;
		this.ratingAvg = BigDecimal.ZERO;
		this.totalReview = 0;
	}

	/* ================== 생성자 2개(유료/무료) ================== */

	// 무료 스토어 생성
	public static Store createFreeStore(
		Long storeManagerId,
		String name,
		String address,
		BigDecimal latitude,
		BigDecimal longitude,
		LocalDate startDate,
		LocalDate endDate) {

		return Store.builder()
			.storeManagerId(storeManagerId)
			.name(name)
			.address(address)
			.latitude(latitude)
			.longitude(longitude)
			.startDate(startDate)
			.endDate(endDate)
			.isFree(true)
			.price(null)     // 무료 → 가격 없음
			.build();
	}

	// 유료 스토어 생성
	public static Store createPaidStore(
		Long storeManagerId,
		String name,
		String address,
		BigDecimal latitude,
		BigDecimal longitude,
		LocalDate startDate,
		LocalDate endDate,
		int price) {

		return Store.builder()
			.storeManagerId(storeManagerId)
			.name(name)
			.address(address)
			.latitude(latitude)
			.longitude(longitude)
			.startDate(startDate)
			.endDate(endDate)
			.isFree(false)
			.price(price)
			.build();
	}

	/* ================== 도메인 수정 메서드 ================== */

	//요청값이 하나면 하나만 수정되게
	public void updateStore(
		String name,
		String address,
		BigDecimal latitude,
		BigDecimal longitude,
		LocalDate startDate,
		LocalDate endDate,
		StoreStatus status,
		Integer price,
		String imageUrl,
		String description
	) {
		if (name != null) this.name = name;
		if (address != null) this.address = address;
		if (latitude != null) this.latitude = latitude;
		if (longitude != null) this.longitude = longitude;
		if (startDate != null) this.startDate = startDate;
		if (endDate != null) this.endDate = endDate;
		if (status != null) this.status = status;
		if (price != null) {this.isFree = false;this.price = price;}
		else {this.isFree = true; this.price = null;}
		if (imageUrl != null) this.imageUrl = imageUrl;
		if (description != null) this.description = description;
	}

	//리뷰 생성시 평점 업데이트
	public void increaseRating(Integer rating) {
		BigDecimal intRating = BigDecimal.valueOf(rating);
		BigDecimal total = BigDecimal.valueOf(this.totalReview);

		//기존 총합 + 새 평점 / 기존 리뷰수 + 1, 소수점 첫째자리까지 반올림
		BigDecimal sum = this.ratingAvg.multiply(total).add(intRating);
		BigDecimal newAvg = sum.divide(total.add(BigDecimal.ONE), 1, RoundingMode.HALF_UP);

		this.totalReview += 1;
		this.ratingAvg = newAvg;
	}
	//리뷰 삭제시 평점 업데이트
	public void decreaseRating(Integer rating) {
		//리뷰가 1개 남아있을 때 삭제된 경우 ratingAvg,totalReview = 0으로 만듦
		if(this.totalReview <= 1) {
			this.totalReview = 0;
			this.ratingAvg = BigDecimal.ZERO;
			return;
		}
		BigDecimal intRating = BigDecimal.valueOf(rating);
		BigDecimal total = BigDecimal.valueOf(this.totalReview);

		BigDecimal sum = this.ratingAvg.multiply(total).subtract(intRating);
		BigDecimal newAvg = sum.divide(total.subtract(BigDecimal.ONE), 1, RoundingMode.HALF_UP);

		this.totalReview -= 1;
		this.ratingAvg = newAvg;
	}

	//storeManager 판별
	public boolean isNotManagedBy(Long userId) {
		return this.storeManagerId == null || !this.storeManagerId.equals(userId);
	}
}
