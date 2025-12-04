package com.popjub.store_service.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.popjub.store_service.domain.entity.TimeSlot;

public interface TimeSlotJpaRepository extends JpaRepository<TimeSlot, UUID> {
}
