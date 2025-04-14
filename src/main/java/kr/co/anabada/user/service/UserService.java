package kr.co.anabada.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.admin.entity.Warn;
import kr.co.anabada.admin.repository.WarnRepository;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private WarnRepository warnRepo;
    
    // 사용자 No으로 조회
    public User getUser(Integer userNo) {
        return userRepository.findById(userNo).orElse(null);
    }

    // 사용자 ID로 조회
    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    // 사용자 이메일로 조회
    public User findByEmail(String email) {
        return userRepository.findByUserEmail(email);
    }

    // 사용자 저장
    public void saveUser(User user) {
        userRepository.save(user);
    }
    
    // 경고받은 횟수 추가
    public boolean plusWarnCtn(String warnNo) {
        Warn warn = warnRepo.findByWarnNo(Integer.valueOf(warnNo));
        User user = warn.getWarnDefendantUser();
        
        byte currentCnt = user.getUserWarnCnt();
        
        if (currentCnt == Byte.MAX_VALUE) {
            return false;
        }
        
        user.setUserWarnCnt((byte) (currentCnt + 1));
        userRepository.save(user);
        
        return true;
    }
}
