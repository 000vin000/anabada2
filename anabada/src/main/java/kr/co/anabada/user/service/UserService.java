package kr.co.anabada.user.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.mapper.UserMapper;
import kr.co.anabada.user.util.PasswordHasher; // PBKDF2 비밀번호 암호화 유틸리티 클래스 임포트

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
    @Autowired
    private UserMapper userMapper;

    // 회원가입 로직
    public String joinUser(User user) {
        if (userMapper.selectByUserId(user.getUserId()) != null) {
            return "이미 존재하는 아이디입니다.";
        }
        try {
            // 비밀번호를 PBKDF2 방식으로 암호화
            String hashedPassword = PasswordHasher.hashPassword(user.getUserPw());
            user.setUserPw(hashedPassword); // 암호화된 비밀번호를 User 객체에 설정
            userMapper.insertUser(user); // DB에 사용자 정보 저장
            logger.info("회원가입 성공: {}", user.getUserId());
            return "회원가입 성공";
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error("비밀번호 암호화 오류", e);
            return "회원가입 실패: 비밀번호 암호화 오류";
        } catch (Exception e) {
            logger.error("회원가입 중 예외 발생", e);
            return "회원가입 실패: " + e.getMessage();
        }
    }
    // 아이디 중복 확인
    public boolean isUserIdDuplicate(String userId) {
        return userMapper.selectByUserId(userId) != null;
    }
    // 닉네임 중복 확인
    public boolean isUserNickDuplicate(String userNick) {
        return userMapper.selectByUserNick(userNick) != null;
    }

    // 이메일 중복 확인
    public boolean isUserEmailDuplicate(String userEmail) {
        return userMapper.selectByUserEmail(userEmail) != null;
    }

    // 전화번호 중복 확인
    public boolean isUserPhoneDuplicate(String userPhone) {
        // 입력값에서 "-" 제거
        userPhone = userPhone.replaceAll("-", "");
        return userMapper.selectByUserPhone(userPhone) != null;
    }
//    public boolean isUserPhoneDuplicate(String userPhone) {
//        return userMapper.selectByUserPhone(userPhone) != null;
//    }
   
    
    // 로그인 로직
    public String loginUser(String userId, String userPw) {
        User user = userMapper.selectByUserId(userId);
        if (user == null) {
            return "존재하지 않는 사용자입니다.";
        }
        try {
            // 입력된 비밀번호와 DB에 저장된 암호화된 비밀번호 비교
            if (PasswordHasher.verifyPassword(userPw, user.getUserPw())) {
                return "로그인 성공";
            } else {
                return "비밀번호가 일치하지 않습니다.";
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace(); // 예외 발생 시 로그 출력
            return "로그인 실패: 비밀번호 검증 오류";
        }
    }
    
    //사용자 ID로 전체 사용자 정보를 조회하는 메소드 추가
    public User getUserByUserId(String userId) {
        return userMapper.selectByUserId(userId);
    }
    
   	
}