package kr.co.anabada.mypage.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.mypage.mapper.UpdateInfoMapper;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.mapper.UserMapper;
import kr.co.anabada.user.util.PasswordHasher;

@Service
public class UpdateInfoService {

    @Autowired
    private UpdateInfoMapper updateInfoMapper;

    @Autowired
    private UserMapper userMapper;
    
    public boolean deactivateUser(int userNo) {
        LocalDateTime now = LocalDateTime.now();
        int rowsAffected = updateInfoMapper.deactivateUser(userNo, now);
        return rowsAffected > 0;
    }
    
    public String updateUserInfo(User updatedUser, String userId) {
        System.out.println("UpdateInfoService - updateUserInfo: 메서드 시작");
        System.out.println("UpdateInfoService - updateUserInfo: 업데이트할 사용자 정보: " + updatedUser);
        
        User existingUser = userMapper.selectByUserId(userId);
        if (existingUser == null) {
            System.out.println("UpdateInfoService - updateUserInfo: 존재하지 않는 사용자");
            return "존재하지 않는 사용자입니다.";
        }

        try {
            userMapper.updateUser(updatedUser);
            System.out.println("UpdateInfoService - updateUserInfo: 사용자 정보 업데이트 성공");
            return "회원정보 변경 성공";
        } catch (Exception e) {
            System.out.println("UpdateInfoService - updateUserInfo: 사용자 정보 업데이트 실패");
            e.printStackTrace();
            return "회원정보 변경 중 오류가 발생했습니다.";
        }
    }

    
    public String changePassword(String userId, String currentPassword, String newPassword) {
        User user = userMapper.selectByUserId(userId);
        if (user == null) {
            return "존재하지 않는 사용자입니다.";
        }

        try {
            if (!PasswordHasher.verifyPassword(currentPassword, user.getUserPw())) {
                return "현재 비밀번호가 일치하지 않습니다.";
            }

            String hashedNewPassword = PasswordHasher.hashPassword(newPassword);
            user.setUserPw(hashedNewPassword);
            userMapper.updateUser(user);
            
            return "비밀번호가 성공적으로 변경되었습니다.";
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return "비밀번호 변경 중 오류가 발생했습니다.";
        }
    }

}