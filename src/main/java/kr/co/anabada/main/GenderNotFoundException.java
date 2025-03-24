package kr.co.anabada.main;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 이 예외는 500 상태 코드를 반환하도록 지정합니다.
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class GenderNotFoundException extends RuntimeException {
    public GenderNotFoundException(String message) {
        super(message);
    }
}

