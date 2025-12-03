package com.popjub.store_service.application.dto.command;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.popjub.store_service.domain.entity.Store;

//Store의 입력값이 ‘단순 객체 생성’이 아니라 ‘유스케이스 전체를 표현하는 복합 입력값’이기 때문
// Store 생성은 “그냥 엔티티 하나 만드는 일”이 아니다.
// 하나의 요청 안에서 3~4개의 작업이 동시에 일어나는 복합 유스케이스
public record CreateStoreCommand(
	UUID storeManagerId,
	String storeName,
	String address,
	BigDecimal latitude,
	BigDecimal longitude,
	LocalDate startDate,
	LocalDate endDate,
	Boolean isFree,
	Integer price
) {
	public Store toEntity(){
		if(isFree){
			return Store.createFreeStore(
				storeManagerId,
				storeName,
				address,
				latitude,
				longitude,
				startDate,
				endDate
			);
		}
		return Store.createPaidStore(
			storeManagerId,
			storeName,
			address,
			latitude,
			longitude,
			startDate,
			endDate,
			price
		);
	}
}
