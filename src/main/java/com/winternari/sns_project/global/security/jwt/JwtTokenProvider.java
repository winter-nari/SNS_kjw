package com.winternari.sns_project.global.security.jwt;

import com.winternari.sns_project.domain.user.entity.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String secretKeyPlain;

    private Key key;

    private static final long ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 60;       // 1시간
    private static final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24; // 1일

    @PostConstruct
    public void init() {
        // Base64 디코딩 후 키 생성 (환경변수에서 넣을 때 base64 인코딩된 문자열을 넣도록 유도)
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyPlain);
        this.key = Keys.hmacShaKeyFor(decodedKey);
    }

    public String createAccessToken(UserEntity user) {
        return createToken(user.getId(), ACCESS_TOKEN_VALID_TIME, "ACCESS_TOKEN");
    }

    public String createRefreshToken(UserEntity user) {
        return createToken(user.getId(), REFRESH_TOKEN_VALID_TIME, "REFRESH_TOKEN");
    }

    private String createToken(UUID userId, long validity, String type) {

        Date now = new Date();
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validity))
                .claim("type", type)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String type = claims.get("type", String.class);
            if (!"ACCESS_TOKEN".equals(type) && !"REFRESH_TOKEN".equals(type)) {
                throw new JwtException("유효하지 않은 토큰 타입입니다.");
            }

            return true;

        } catch (ExpiredJwtException e) {
            log.warn("[JWT 만료] {}", e.getMessage());
        } catch (JwtException | IllegalArgumentException e) {
            // 민감 정보 노출 방지: 메시지에 토큰 내용이 노출될 수 있으므로 간략히.
            log.warn("[JWT 오류] 유효하지 않은 JWT");
        }
        return false;
    }


    public String getUserIdFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
