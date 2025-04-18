package kr.co.anabada.common.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.co.anabada.item.exception.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	@Getter
	@AllArgsConstructor
	private static class ErrorResponse {
		private int status;
		private String message;
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
		log.warn("리소스 검색 실패: {}", ex.getMessage());
		ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<ErrorResponse> handleInvalidRequest(InvalidRequestException ex) {
		log.warn("유효하지 않은 요청: {}", ex.getMessage());
		ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AuthenticationRequiredException.class)
	public ResponseEntity<ErrorResponse> handleAuthenticationRequired(AuthenticationRequiredException ex) {
		log.warn("인증 필요: {}", ex.getMessage());
		ErrorResponse response = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(BidException.class)
	public ResponseEntity<ErrorResponse> handleBid(BidException ex) {
		log.warn("입찰 오류: {}", ex.getMessage());
		ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
		log.warn("접근 권한 없음: {}", ex.getMessage());
		ErrorResponse response = new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}
}
