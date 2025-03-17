package kr.co.anabada.mypage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.mapper.UserMapper;
import kr.co.anabada.user.util.PasswordHasher;

@Service
public class PasswordCheckService {

    @Autowired
    private UserMapper userMapper;

    public boolean checkPassword(String inputPassword, String userId) {
        User user = userMapper.selectByUserId(userId);
        if (user == null) {
            return false;
        }

        try {
            return PasswordHasher.verifyPassword(inputPassword, user.getUserPw());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
