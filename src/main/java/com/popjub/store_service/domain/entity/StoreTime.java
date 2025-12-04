package com.popjub.store_service.domain.entity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.popjub.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_store_time")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class StoreTime  extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID storeTimeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@Column(nullable = false)
	private LocalDate date;

	/**
	 * 요일은 java.time.DayOfWeek 로 관리
	 * DB 컬럼은 ENUM STRING 으로 "MONDAY", "TUESDAY" 형태로 저장됨
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "day_of_week", nullable = false, length = 9)
	private DayOfWeek dayOfWeek;

	@Column(name = "start_time", nullable = false)
	private LocalTime startTime;

	@Column(name = "end_time", nullable = false)
	private LocalTime endTime;

	/* ================== private 생성자 ================== */

	@Builder(access = AccessLevel.PRIVATE)
	private StoreTime(Store store,
		LocalDate date,
		DayOfWeek dayOfWeek,
		LocalTime startTime,
		LocalTime endTime) {
		this.store = store;
		this.date = date;
		this.dayOfWeek = dayOfWeek;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	/* ================== 정적 팩토리 메서드 ================== */

	/**
	 * LocalDate로만 받아서 내부에서 요일 계산하는 버전
	 * (운영기간 + 운영시간 조합해서 생성할 때 사용)
	 */
	public static StoreTime of(
		Store store,
		LocalDate date,
		LocalTime startTime,
		LocalTime endTime) {

		return StoreTime.builder()
			.store(store)
			.date(date)
			.dayOfWeek(date.getDayOfWeek())
			.startTime(startTime)
			.endTime(endTime)
			.build();
	}

	/* ================== 도메인 메서드 ================== */

	/**
	 * 운영 시간/날짜 수정 (요일은 date 기준으로 재계산)
	 */
	public void updateOperatingTime(LocalDate date,
		LocalTime startTime,
		LocalTime endTime) {

		this.date = date;
		this.dayOfWeek = date.getDayOfWeek();
		this.startTime = startTime;
		this.endTime = endTime;
	}
}
