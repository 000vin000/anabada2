package kr.co.anabada.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordDTO {

    @NotBlank(message = "현재 비밀번호는 필수 입력입니다.")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호는 필수 입력입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    private String newPassword;
}
