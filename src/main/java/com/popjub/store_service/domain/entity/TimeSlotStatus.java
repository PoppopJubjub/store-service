package com.popjub.store_service.domain.entity;

public enum TimeSlotStatus {
	AVAILABLE, // 예약 가능
	FULL,      // 예약 마감
	CLOSED     // 닫힘 (운영 시간 외 등)
}
