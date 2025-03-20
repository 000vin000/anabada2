package kr.co.anabada.user.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserLoginRepository;

@Service
public class UserLoginService {

    @Autowired
    private UserLoginRepository userLoginRepository;

    public Optional<User> findByUserId(String userId) {
        return userLoginRepository.findByUserId(userId);
    }
}
