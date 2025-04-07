package kr.co.anabada.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndividualUserLoginDTO {
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String userPw;
}
