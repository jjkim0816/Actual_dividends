package com.zerobase.dividends.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    private static final long TOKEN_EXPIRED_TIME = 1000 * 60 * 60; // 1 hour
    private static final String KEY_ROLES = "roles";
    @Value("${spring.jwt.secret}")
    private String  secretKey;

    public String generateToken(String username, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(KEY_ROLES, roles);

        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + TOKEN_EXPIRED_TIME);

        return Jwts.builder().setClaims(claims)
            .setIssuedAt(now) // 토큰 생성 시간
            .setExpiration(expiredDate) // 토큰 만료 시간
            .signWith(SignatureAlgorithm.HS512, this.secretKey) // 사용할 암호화 알고리즘, 비밀비
            .compact();
    }

    public String getUser(String token) {
        return this.parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        Claims claims = this.parseClaims(token);

        return !claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) {
        try {
            return  Jwts.parser()
                        .setSigningKey(this.secretKey)
                        .parseClaimsJws(token)
                        .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
