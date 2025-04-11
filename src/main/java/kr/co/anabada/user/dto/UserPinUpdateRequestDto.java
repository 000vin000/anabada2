package kr.co.anabada.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPinUpdateRequestDto {

    @NotBlank(message = "새로운 2차 비밀번호는 필수입니다.")
    private String newPin;

}
