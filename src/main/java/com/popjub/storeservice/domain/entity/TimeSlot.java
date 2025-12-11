package com.popjub.storeservice.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import com.popjub.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_timeslot")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeSlot extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID timeslotId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@Column(name = "date", nullable = false)
	private LocalDate date;

	@Column(name = "start_time", nullable = false)
	private LocalTime startTime;

	@Column(name = "interval_minutes", nullable = false)
	private Integer interval;  // 30 or 60

	@Column(name = "capacity", nullable = false)
	private Integer capacity;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	private TimeSlotStatus status = TimeSlotStatus.AVAILABLE;

	/* ================== 생성자 ================== */

	@Builder(access = AccessLevel.PRIVATE)
	private TimeSlot(
		Store store,
		LocalDate date,
		LocalTime startTime,
		Integer interval,
		Integer capacity
	) {
		this.store = store;
		this.date = date;
		this.startTime = startTime;
		this.interval = interval;
		this.capacity = capacity;
	}

	/* ================== 정적 팩토리 ================== */

	public static TimeSlot of(
		Store store,
		LocalDate date,
		LocalTime startTime,
		Integer interval,
		Integer capacity
	) {
		return TimeSlot.builder()
			.store(store)
			.date(date)
			.startTime(startTime)
			.interval(interval)
			.capacity(capacity)
			.build();
	}
	/* ================== 상태 변경 메서드 ================== */

	public void close() {
		this.status = TimeSlotStatus.CLOSED;
	}

	public void makeFull() {
		this.status = TimeSlotStatus.FULL;
	}

	public void makeAvailable() {
		this.status = TimeSlotStatus.AVAILABLE;
	}

	public boolean doesNotBelongTo(Store store) {
		return this.store == null || !this.store.equals(store);
	}

	//체크인 검증 로직
	public boolean isNotWithinCheckInWindow(LocalDateTime now) {
		LocalDateTime slotStart = LocalDateTime.of(this.getDate(), this.getStartTime());
		LocalDateTime slotEnd = slotStart.plusMinutes(20);

		// 시작 전에 오거나, 10분 이후면 허용 X
		return now.isBefore(slotStart) || now.isAfter(slotEnd);
	}
}
