package com.leedae.mockaroo.oauth2;

import com.leedae.mockaroo.doamin.User;
import com.leedae.mockaroo.doamin.constant.SocialType;
import com.leedae.mockaroo.dto.oauth2.OAuth2Attributes;
import com.leedae.mockaroo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oauth2User);
        } catch (Exception e) {
            log.error("OAuth2 로그인 처리 중 에러 발생", e);
            throw new OAuth2AuthenticationException(e.getMessage());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oauth2User) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);

        OAuth2Attributes attributes = OAuth2Attributes.of(socialType, oauth2User.getAttributes());

        User user = saveOrUpdate(attributes, socialType);

        return UserPrincipal.create(user, oauth2User.getAttributes());
    }

    private User saveOrUpdate(OAuth2Attributes attributes, SocialType socialType) {
        User user = userRepository.findByEmailAndSocialType(attributes.getEmail(), socialType)
                .map(entity -> entity.updateOAuth2Profile(attributes.getName(), attributes.getProfileImage()))
                .orElse(attributes.toEntity(socialType, passwordEncoder));

        return userRepository.save(user);
    }

    private SocialType getSocialType(String registrationId) {
        return SocialType.valueOf(registrationId.toUpperCase());
    }
}