package com.leedae.mockaroo.dto.oauth2;

import com.leedae.mockaroo.doamin.User;
import com.leedae.mockaroo.doamin.constant.SocialType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.UUID;

@Getter
@Builder
public class OAuth2Attributes {
    private String nameAttributeKey;
    private String email;
    private String name;
    private String profileImage;

    public static OAuth2Attributes of(SocialType socialType, Map<String, Object> attributes) {
        return switch (socialType) {
            case KAKAO -> ofKakao(attributes);
            case NAVER -> ofNaver(attributes);
            default -> throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다.");
        };
    }

    private static OAuth2Attributes ofKakao(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attributes.builder()
                .nameAttributeKey("id")
                .email((String) kakaoAccount.get("email"))
                .name((String) profile.get("nickname"))
                .profileImage((String) profile.get("profile_image_url"))
                .build();
    }

    private static OAuth2Attributes ofNaver(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2Attributes.builder()
                .nameAttributeKey("id")
                .email((String) response.get("email"))
                .name((String) response.get("name"))
                .profileImage((String) response.get("profile_image"))
                .build();
    }

    public User toEntity(SocialType socialType, PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(email)
                .name(name)
                .nickname(name)
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .profileImage(profileImage)
                .socialType(socialType)
                .build();
    }
}