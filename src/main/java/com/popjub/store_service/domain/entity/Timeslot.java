package com.popjub.store_service.domain.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_timeslot")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE p_timeslot SET deleted_at = NOW(), deleted_by = ? WHERE timeslot_id = ?")
@Where(clause = "deleted_at IS NULL")
public class Timeslot /*extends BaseEntity*/ {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID timeslotId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@Column(name = "start_time", nullable = false)
	private LocalTime startTime;

	@Column(name = "interval", nullable = false)
	private LocalTime interval;  // 분 단위 (30 또는 60)

	@Column(name = "capacity", nullable = false)
	private Integer capacity;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	private TimeslotStatus status = TimeslotStatus.AVAILABLE;

	/* ================== 생성자 ================== */

	public Timeslot(Store store, LocalTime startTime, LocalTime interval, Integer capacity) {
		this.store = store;
		this.startTime = startTime;
		this.interval = interval;
		this.capacity = capacity;
		validate();
	}

	/* ================== 도메인 검증 ================== */

	@PrePersist
	@PreUpdate
	private void validateBeforeSave() {
		validate();
	}

	private void validate() {
		validateInterval();
		validateCapacity();
	}

	private void validateInterval() {
		if (interval == null || !List.of(30, 60).contains(interval)) {
			throw new IllegalArgumentException("interval은 30 또는 60만 허용됩니다.");
		}
	}

	private void validateCapacity() {
		if (capacity == null || capacity <= 0) {
			throw new IllegalArgumentException("capacity는 1 이상이어야 합니다.");
		}
	}

	/* ================== 도메인 수정 메서드 ================== */

	public void updateSlot(LocalTime startTime, LocalTime interval, Integer capacity) {
		this.startTime = startTime;
		this.interval = interval;
		this.capacity = capacity;
		validate();
	}

	/* ================== 편의 메서드 ================== */
	public void close() {
		this.status = TimeslotStatus.CLOSED;
	}

	public void makeFull() {
		this.status = TimeslotStatus.FULL;
	}

	public void makeAvailable() {
		this.status = TimeslotStatus.AVAILABLE;
	}
}