package kr.co.anabada.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPinRequestDto {

    @NotBlank(message = "2차 비밀번호는 필수입니다.")
    @Pattern(regexp = "^[0-9]{6}$", message = "2차 비밀번호는 6자리 숫자여야 합니다.")
    private String userPin;
}
