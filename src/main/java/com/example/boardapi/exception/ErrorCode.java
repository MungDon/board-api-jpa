package com.example.boardapi.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST,"입력 정보 유효성 검증에 실패하였습니다."),
	INSERT_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"저장 작업 실패"),
	UPDATE_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"수정 작업 실패"),
	DELETE_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"삭제 작업 실패");
	
	private final  HttpStatus status;
	private final String message;
}
