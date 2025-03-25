package kr.co.anabada.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserTokenInfo {
    private String userId;
    private Long userNo;
    private String userType;
    private String nickname;
}
//반환 DTO
