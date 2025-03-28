package kr.co.anabada.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoUpdateRequestDto {

    @NotBlank(message = "이름은 필수입니다.")
    private String userName;

    @NotBlank(message = "닉네임은 필수입니다.")
    private String userNick;

    @NotBlank(message = "전화번호는 필수입니다.")
    private String userPhone;

    @NotBlank(message = "주소는 필수입니다.")
    private String userAddress;
}
