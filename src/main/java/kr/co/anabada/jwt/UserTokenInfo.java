package kr.co.anabada.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserTokenInfo {
    private final String userId;
    private final Integer userNo;
    private final String userType;
    private final String nickname;
    
}

