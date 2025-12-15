package com.popjub.storeservice.application.listner;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.popjub.storeservice.application.event.TimeSlotCloseEvent;
import com.popjub.storeservice.infrastructure.client.ReservationServiceClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TimeSlotCloseNotifier {

	private final ReservationServiceClient client;

	//트랜잭션이 정상적으로 COMMIT 된 이후 예약측에 데이터 전송
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void onClosed(TimeSlotCloseEvent event) {
		client.sendClosedIds(event.timeslotIds());
	}
}
