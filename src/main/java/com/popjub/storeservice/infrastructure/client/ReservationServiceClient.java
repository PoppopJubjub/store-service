package com.popjub.storeservice.infrastructure.client;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "reservation-service")
public interface ReservationServiceClient {

	@PostMapping("/internal/reservations/capacities")
	Map<UUID, Integer> getRemaining(
		@RequestBody List<UUID> timeslotIds
	);

	@PostMapping("/internal/reservations/timeslots/closed")
	void sendClosedIds(@RequestBody List<UUID> timeslotIds);

}
