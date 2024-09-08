package com.example.boardapi.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST,"입력 정보 유효성 검증에 실패하였습니다."),
	UTF8_ENCODING_NOT_SUPPORTED(HttpStatus.BAD_REQUEST,"UTF-8 인코딩이 지원되지 않는 데이터입니다."),

	TOKEN_FORMAT_ERROR(HttpStatus.BAD_REQUEST,"토큰의 형식이 올바르지않습니다."),
	TOKEN_NOT_VALID(HttpStatus.BAD_REQUEST,"유효하지 않은 토큰입니다."),
	REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,"리프레쉬 토큰이 만료되었습니다."),
	REFRESH_TOKEN_NOT_VALID(HttpStatus.BAD_REQUEST,"유효하지 않은 리프레쉬 토큰입니다."),

	INSERT_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"저장 작업 실패"),
	UPDATE_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"수정 작업 실패"),
	DELETE_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"삭제 작업 실패");
	
	private final  HttpStatus status;
	private final String message;
}
