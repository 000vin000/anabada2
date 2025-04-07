package kr.co.anabada.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import kr.co.anabada.user.entity.User.UserType;

@Getter
@Setter
public class IndividualUserJoinDTO {
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String userPw;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String userName;

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    private String userNick;

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String userEmail;

    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    private String userPhone;

    // 회원가입 시에는 주소를 기본주소와 상세주소로 나누어서 받아서, 백엔드에서 합친다.
    @NotBlank(message = "기본 주소는 필수 입력값입니다.")
    private String baseAddress;

    @NotBlank(message = "상세 주소는 필수 입력값입니다.")
    private String detailAddress;

    // ENUM 타입
    private UserType userType = UserType.INDIVIDUAL; // 기본값 설정
}
