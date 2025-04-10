package kr.co.anabada.item.exception;

public class AuthenticationRequiredException extends RuntimeException {
	public AuthenticationRequiredException(String message) {
		super(message);
	}
}
