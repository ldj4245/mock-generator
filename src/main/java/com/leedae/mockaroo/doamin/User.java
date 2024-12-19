package com.leedae.mockaroo.doamin;

import com.leedae.mockaroo.doamin.constant.SocialType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Column(nullable = false)
    private String name;

    private String nickname;

    private String profileImage;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<MockData> mockDataList = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private RefreshToken refreshToken;

    @Builder
    public User(String email, String password, String name, String nickname,
                String profileImage, SocialType socialType, String socialId) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.socialType = socialType != null ? socialType : SocialType.NONE;
        this.socialId = socialId;
    }

    // OAuth2 로그인 시 프로필 업데이트
    public User updateOAuth2Profile(String name, String profileImage) {
        this.name = name;
        if (profileImage != null) {
            this.profileImage = profileImage;
        }
        return this;
    }

    // 사용자가 직접 프로필 수정할 때
    public User updateProfile(String nickname, String profileImage) {
        this.nickname = nickname;
        if (profileImage != null) {
            this.profileImage = profileImage;
        }
        return this;
    }

}