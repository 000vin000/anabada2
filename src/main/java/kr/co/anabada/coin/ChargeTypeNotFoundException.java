package kr.co.anabada.coin;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ChargeTypeNotFoundException extends RuntimeException {
	public ChargeTypeNotFoundException(String message) {
        super(message);
    }
}
