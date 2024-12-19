package com.leedae.mockaroo.oauth2.handler;

import com.leedae.mockaroo.config.jwt.TokenProvider;
import com.leedae.mockaroo.oauth2.UserPrincipal;
import com.leedae.mockaroo.utils.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private static final String REDIRECT_URI = "http://localhost:8080/home"; // Thymeleaf 뷰 경로로 수정

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String token = tokenProvider.createToken(authentication);

        // JWT 토큰을 쿠키에 저장
        CookieUtils.addCookie(response, "token", token, 3600);

        String targetUrl = UriComponentsBuilder.fromUriString(REDIRECT_URI)
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}