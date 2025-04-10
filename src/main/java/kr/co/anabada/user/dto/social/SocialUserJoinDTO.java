package kr.co.anabada.user.dto.social;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import kr.co.anabada.user.entity.User.UserType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialUserJoinDTO {

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String userName;

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    private String userNick;

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String userEmail;

    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    private String userPhone;

    // 기본 주소와 상세 주소로 분리
    @NotBlank(message = "기본 주소는 필수 입력값입니다.")
    private String baseAddress;

    @NotBlank(message = "상세 주소는 필수 입력값입니다.")
    private String detailAddress;

    // ENUM 기본값: INDIVIDUAL
    private UserType userType = UserType.INDIVIDUAL;
}
