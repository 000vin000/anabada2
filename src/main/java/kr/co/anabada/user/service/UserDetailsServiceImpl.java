package kr.co.anabada.user.service;

import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserLoginRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserLoginRepository userLoginRepository; // ✅ UserRepository → UserLoginRepository로 변경

    public UserDetailsServiceImpl(UserLoginRepository userLoginRepository) {
        this.userLoginRepository = userLoginRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userLoginRepository.findByUserId(username) // ✅ UserLoginRepository 활용
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserId())
                .password(user.getUserPw()) // ✅ userPw 필드 기준
                .authorities("ROLE_USER")
                .build();
    }
}
