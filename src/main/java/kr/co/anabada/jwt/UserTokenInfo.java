package kr.co.anabada.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserTokenInfo {
    private String userId;
    private Integer userNo;
    private String userType;
    private String nickname;
}
