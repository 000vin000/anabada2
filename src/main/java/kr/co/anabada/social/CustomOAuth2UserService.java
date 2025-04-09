package kr.co.anabada.social;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import kr.co.anabada.social.dto.OAuthAttributes;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.social.SocialUserLoginRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final SocialUserLoginRepository socialUserLoginRepository;

    public CustomOAuth2UserService(SocialUserLoginRepository socialUserLoginRepository) {
        this.socialUserLoginRepository = socialUserLoginRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("소셜 로그인 요청: {}", userRequest.getClientRegistration().getRegistrationId());
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("소셜 로그인 사용자 정보: {}", oAuth2User.getAttributes());
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuthAttributes oAuthAttributes = null;
        if ("google".equals(registrationId)) {
            oAuthAttributes = OAuthAttributes.ofGoogle(registrationId, attributes);
        }

        String email = oAuthAttributes.getEmail();
        String name = oAuthAttributes.getName();

        Optional<User> optionalUser = socialUserLoginRepository.findByUserEmail(email);

        if (optionalUser.isEmpty()) {
            return new CustomOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_SOCIAL")),
                Map.of(
                    "email", email,
                    "name", name,
                    "isNew", true
                ),
                "email"
            );
        } else {
            User user = optionalUser.get();
            return new CustomOAuth2User(
                List.of(new SimpleGrantedAuthority(user.getRole())),
                Map.of(
                    "email", user.getUserEmail(),
                    "name", user.getUserNick(),
                    "userNo", user.getUserNo(),
                    "isNew", false
                ),
                "email"
            );
        }
    }
}
