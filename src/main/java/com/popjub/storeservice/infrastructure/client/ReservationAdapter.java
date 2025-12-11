package com.popjub.storeservice.infrastructure.client;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.popjub.storeservice.application.port.ReservationServicePort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReservationAdapter implements ReservationServicePort {

	private final ReservationServiceClient  reservationServiceClient;


	@Override
	public Map<UUID, Integer> getCapacities(List<UUID> timeslotIds) {
		if(timeslotIds == null || timeslotIds.isEmpty()) {
			return Collections.emptyMap();
		}
		return reservationServiceClient.getRemaining(timeslotIds);
	}
}
