package com.popjub.storeservice.application.port;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ReservationServicePort {
	Map<UUID, Integer> getCapacities(List<UUID> timeslotIds);
}
