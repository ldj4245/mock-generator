package com.leedae.mockaroo.dto.response;

import com.leedae.mockaroo.doamin.User;
import com.leedae.mockaroo.doamin.constant.SocialType;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String profileImage;
    private SocialType socialType;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .socialType(user.getSocialType())
                .build();
    }
}