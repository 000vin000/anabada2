package kr.co.anabada.user.service;

import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.entity.User.UserType;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
public class UserDetailsImpl implements UserDetails {

    private final User user;
    private final UserType userType; 

    public UserDetailsImpl(User user) {
        this.user = user;
        this.userType = user.getUserType();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	 Collection<? extends GrantedAuthority> authorities = 
    		        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getUserType().name()));

    		    return authorities;
    }
    
    public User getUser() {
    	return user;
    }
    
    @Override
    public String getPassword() {
        return user.getUserPw();
    }

    @Override
    public String getUsername() {
        return user.getUserId();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }

    // 사용자 상태 기반으로 반환
    @Override
    public boolean isEnabled() {
        return user.getUserStatus() == User.UserStatus.ACTIVE;
    }
}
