package com.leedae.mockaroo.doamin;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refresh_tokens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseTimeEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public RefreshToken(String id, String token, User user) {
        this.id = id;
        this.token = token;
        this.user = user;
    }

    public void updateToken(String token) {
        this.token = token;
    }
}