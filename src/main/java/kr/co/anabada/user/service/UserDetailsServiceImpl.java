package kr.co.anabada.user.service;

import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserLoginRepository;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserLoginRepository userLoginRepository;

    public UserDetailsServiceImpl(UserLoginRepository userLoginRepository) {
        this.userLoginRepository = userLoginRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userLoginRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserName())
                .password(user.getUserPw())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(user.getUserType().getRole())))  // Enum에서 역할 가져오기
                .build();
    }
}
