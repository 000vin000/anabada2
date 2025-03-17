package kr.co.anabada.mypage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordChangeForm {

    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    @Size(min = 6, message = "비밀번호는 6자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$", 
            message = "비밀번호는 숫자, 문자, 특수문자를 포함하여 6자 이상이어야 합니다.")
    private String newPassword;

    @NotBlank(message = "새 비밀번호 확인을 입력해주세요.")
    private String confirmNewPassword;

}
