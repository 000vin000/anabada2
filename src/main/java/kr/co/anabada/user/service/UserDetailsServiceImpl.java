package kr.co.anabada.user.service;

import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.IndividualUserLoginRepository;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IndividualUserLoginRepository userLoginRepository;

    public UserDetailsServiceImpl(IndividualUserLoginRepository userLoginRepository) {
        this.userLoginRepository = userLoginRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userLoginRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다: " + username));

        //상태 확인 추가
        if (user.getUserStatus() != User.UserStatus.ACTIVE) {
            throw new UsernameNotFoundException("로그인 불가 상태입니다: " + user.getUserStatus());
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserId())
                .password(user.getUserPw())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getUserType().name()))) // 권한 설정
                .build();
    }
}
