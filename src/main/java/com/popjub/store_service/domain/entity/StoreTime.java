package com.popjub.store_service.domain.entity;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
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
@Table(name = "p_store_time")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE p_store_time SET deleted_at = NOW(), deleted_by = ? WHERE store_time_id = ?")
@Where(clause = "deleted_at IS NULL")
public class StoreTime /*extends BaseEntity*/{

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID storeTimeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@Column(name = "day_of_week", nullable = false, length = 4)
	private String dayOfWeek;  // "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"

	// ✅ TIME 타입 → LocalTime으로 매핑
	@Column(name = "start_time", nullable = false)
	private LocalTime startTime;

	// ✅ TIME 타입 → LocalTime으로 매핑
	@Column(name = "end_time", nullable = false)
	private LocalTime endTime;

	/* ================== 생성자 ================== */

	public StoreTime(Store store, String dayOfWeek, LocalTime startTime, LocalTime endTime) {
		this.store = store;
		this.dayOfWeek = dayOfWeek;
		this.startTime = startTime;
		this.endTime = endTime;
		validate();
	}

	/* ================== 도메인 메서드 ================== */

	public void updateOperatingTime(String dayOfWeek, LocalTime startTime, LocalTime endTime) {
		this.dayOfWeek = dayOfWeek;
		this.startTime = startTime;
		this.endTime = endTime;
		validate();
	}

	/* ================== 검증 ================== */

	private void validate() {
		validateDayOfWeek();
		validateTimeRange();
	}

	private void validateDayOfWeek() {
		List<String> validDays = List.of("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN");
		if (!validDays.contains(dayOfWeek)) {
			throw new IllegalArgumentException("유효하지 않은 요일입니다");
		}
	}

	private void validateTimeRange() {
		// ✅ LocalTime.isBefore() 사용 가능
		if (endTime.isBefore(startTime)) {
			throw new IllegalStateException("종료 시간은 시작 시간보다 빠를 수 없습니다.");
		}

		if (endTime.equals(startTime)) {
			throw new IllegalStateException("시작 시간과 종료 시간이 같을 수 없습니다.");
		}
	}

}