package com.example.boardapi.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 *	validate 예외
	 * @param e(MethodArgumentNotValidException)
	 * @return	INVALID_INPUT_VALUE 코드 메세지, 유효성 검사 타켓 메세지
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)   // MethodArgument~~에러가 발생하면 해당 핸들러로 넘어오게됨
	public ResponseEntity<ErrorResponse> ValidErrorException(MethodArgumentNotValidException e){
		final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());// ErrorResponse에 정의해놓은 메서드에 의해 데이터가 반환
		return ResponseEntity.status(ErrorCode.INVALID_INPUT_VALUE.getStatus()).body(response);
	}

	/**
	 * CustomException 커스텀 예외처리
	 * @param e(CustomException) 예외
	 * @return
	 */
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException e){
		
		return ErrorResponse.toResponseEntity(e.getErrorCode());
	}
}
