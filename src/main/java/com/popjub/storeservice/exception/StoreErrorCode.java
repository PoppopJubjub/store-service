package com.popjub.storeservice.exception;

import org.springframework.http.HttpStatus;

import com.popjub.common.exception.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreErrorCode implements BaseErrorCode {

	/**
	 * 스토어
	 */
	INVALID_LATITUDE("위도는 -90 ~ 90 범위여야 합니다.",  HttpStatus.BAD_REQUEST),
	INVALID_LONGITUDE("경도는 -180 ~ 180 범위여야 합니다.", HttpStatus.BAD_REQUEST),
	INVALID_OPERATION_PERIOD("운영 종료일은 시작일보다 빠를 수 없습니다.", HttpStatus.BAD_REQUEST),
	INVALID_PRICE_POLICY("유료 스토어는 1 이상 가격이 필요합니다.",  HttpStatus.BAD_REQUEST),
	INVALID_CATEGORY_IDS("존재하지 않는 카테고리 ID가 포함되어 있습니다.",HttpStatus.BAD_REQUEST),
	ALREADY_EXISTS_CATEGORY("이미 존재하는 카테고리입니다." ,  HttpStatus.BAD_REQUEST),
	NOT_FOUND_STORE("스토어를 찾을 수 없습니다.",  HttpStatus.NOT_FOUND),
	NOT_FOUND_CATEGORY("카테고리를 찾을 수 없습니다." , HttpStatus.NOT_FOUND),
	NOT_FOUND_STORE_TIME("운영시간을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	NOT_FOUND_TIME_SLOT("운영시간을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	CATEGORY_IN_USE("사용중인 카테고리입니다." , HttpStatus.BAD_REQUEST),
	FORBIDDEN_STORE_ACCESS("해당 스토어의 권한이 없습니다.", HttpStatus.FORBIDDEN),
	/**
	 * 타임룰/타임슬롯 관련 검증 에러
	 */
	INVALID_TIME_RULE_DAYS("모든 요일(MONDAY~SUNDAY)의 운영시간이 필요합니다.", HttpStatus.BAD_REQUEST),
	INVALID_STORE_TIME_RANGE("운영 시작 시간이 종료 시간보다 같거나 늦을 수 없습니다.", HttpStatus.BAD_REQUEST);

	private final String message;
	private final HttpStatus status;
}
