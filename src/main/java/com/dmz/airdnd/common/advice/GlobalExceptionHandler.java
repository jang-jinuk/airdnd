package com.dmz.airdnd.common.advice;

import java.util.Comparator;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dmz.airdnd.common.dto.ApiResponse;
import com.dmz.airdnd.common.exception.BaseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException ex) {
		ApiResponse<Void> response = ApiResponse.failure(ex.getMessage(), ex.getErrorCode().getCode());
		return ResponseEntity.status(ex.getErrorCode().getStatus()).body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
		String errorMessage = ex.getBindingResult()
			.getFieldErrors()
			.stream()
			.sorted(Comparator
				.comparing(FieldError::getField)
				.thenComparing(FieldError::getCode))
			.map(error -> error.getDefaultMessage())
			.findFirst()
			.orElse("요청 형식이 잘못되었습니다.");

		ApiResponse<Void> response = ApiResponse.failure(errorMessage, "INVALID_REQUEST_FORMAT");
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleUnknownException(Exception ex) {
		log.error("알 수 없는 오류가 발생했습니다.", ex);
		ApiResponse<Void> response = ApiResponse.failure("알 수 없는 서버 오류가 발생했습니다.", "INTERNAL_SERVER_ERROR");
		return ResponseEntity.status(500).body(response);
	}
}
