package com.popjub.store_service.application.dto.command;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.popjub.store_service.domain.entity.Store;

public record CreateStoreCommand(
	Long storeManagerId,
	String storeName,
	String address,
	BigDecimal latitude,
	BigDecimal longitude,
	LocalDate startDate,
	LocalDate endDate,
	Boolean isFree,
	Integer price
) {
	//price로 무료/유료 판단은 어떤지?
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
