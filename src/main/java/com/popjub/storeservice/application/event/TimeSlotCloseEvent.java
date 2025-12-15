package com.popjub.storeservice.application.event;

import java.util.List;
import java.util.UUID;

public record TimeSlotCloseEvent(List<UUID> timeslotIds) {
}
