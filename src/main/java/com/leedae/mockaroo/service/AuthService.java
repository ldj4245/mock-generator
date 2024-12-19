package com.leedae.mockaroo.service;

import com.leedae.mockaroo.config.jwt.TokenProvider;
import com.leedae.mockaroo.doamin.User;
import com.leedae.mockaroo.dto.request.LoginRequest;
import com.leedae.mockaroo.dto.request.SignUpRequest;
import com.leedae.mockaroo.dto.response.TokenDto;
import com.leedae.mockaroo.dto.response.UserResponse;
import com.leedae.mockaroo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public UserResponse signup(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 이메일입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .nickname(request.getNickname())
                .build();

        return UserResponse.from(userRepository.save(user));
    }

    public TokenDto login(LoginRequest request) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );

        // 2. 실제로 검증 (사용자 비밀번호 체크)
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        return tokenProvider.createToken(authentication.getName());
    }
}