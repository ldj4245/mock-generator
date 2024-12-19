package com.leedae.mockaroo.config.jwt;

import com.leedae.mockaroo.dto.response.TokenDto;
import com.leedae.mockaroo.oauth2.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;  // 이 부분 추가
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;  // java.sql.Date를 java.util.Date로 변경

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {

    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        long now = (new Date()).getTime();

        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .claim("type", "access")
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtProperties.getAccessTokenValidity()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public TokenDto createToken(String email) {
        long now = (new Date()).getTime();

        String accessToken = Jwts.builder()
                .setSubject(email)
                .claim("type", "access")
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtProperties.getAccessTokenValidity()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(email)
                .claim("type", "refresh")
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtProperties.getRefreshTokenValidity()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(now + jwtProperties.getAccessTokenValidity())
                .build();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return false;
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
