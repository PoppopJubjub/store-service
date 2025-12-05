package com.popjub.store_service.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class StoreCustomException extends RuntimeException {

	private final StoreErrorCode errorCode;
	private final String message;
	private final HttpStatus status;

	public StoreCustomException(StoreErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.message = errorCode.getMessage();
		this.status = errorCode.getStatus();
	}
}
