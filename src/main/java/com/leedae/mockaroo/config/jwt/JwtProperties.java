package com.leedae.mockaroo.config.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "jwt")
@RequiredArgsConstructor
public class JwtProperties {
    private final String secret;
    private final long accessTokenValidity;
    private final long refreshTokenValidity;
}