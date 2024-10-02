package com.newzy.backend.global.auth;

import com.newzy.backend.domain.user.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JwtProvider {
    private final Key key; // JWT 서명을 위한 키
    private final RedisTemplate<String, String> redisTemplate; // RedisTemplate 사용
    private final long TOKEN_EXPIRATION_TIME = 3600; // 토큰 만료 시간 설정 (예: 1시간)

    // 생성자에서 주입된 secretKey로 JWT 서명을 위한 키인 key를 초기화한다.
    public JwtProvider(@Value("${jwt.secretkey}") String secretKey, RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 사용자의 정보를 포함한 JWT 토큰 생성 메소드
    // authentication은 Spring Security의 인증 객체로, 현재 인증된 사용자에 대한 정보를 포함한다.
    public TokenInfo generateToken(User user) {

        // 액세스 토큰의 만료 시간을 설정한다. 일단 현재 시간으로부터 3시간 후로 설정한다.
//        Date accessTokenExpiresIn = new Date((new Date()).getTime() + 1000L * 60 * 60 * 3);
        Date accessTokenExpiresIn = new Date((new Date()).getTime() + TOKEN_EXPIRATION_TIME * 1000);

        // JWT 액세스 토큰을 생성한다.
        String accessToken = Jwts.builder()
                .setSubject(user.getUserId() + "") // 토큰의 주체(Subject)를 설정
                .setExpiration(accessTokenExpiresIn) // 토큰의 만료 시간을 설정
                .signWith(key, SignatureAlgorithm.HS256) // HMAC SHA-256 알고리즘을 사용하여 서명
                .compact();

        // JWT 리프레시 토큰을 생성한다.(expire 시간은 일단 액세스랑 동일하게 설정(이럼 왜씀?))
        String refreshToken = Jwts.builder()
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // JWT 토큰 정보를 포함하는 데이터 클래스인 TokenInfo(user-defined)를 만들어서 반환한다.
        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // JWT 토큰을 파싱하여 인증 정보를 가져오는 메소드다.
    // accessToken은 클라이언트가 전송한 JWT 액세스 토큰이다.
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken); // JWT를 파싱하여 클레임 정보를 가져온다.
        Collection<? extends GrantedAuthority> authorities = new ArrayList(); // 사용자 권한을 저장할 컬렉션이다.
        // UserDetails 객체를 생성한다. 사용자의 이름은 클레임의 주체(Subject)에서 가져오고, 비밀번호와 권한은 빈 값으로 설정한다.
        UserDetails principal = new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities);
        // 이걸로 UsernamePasswordAuthenticationToken 객체를 생성하여 반환한다. 이 객체는 Spring Security에서 인증된 사용자 정보를 나타낸다.
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // JWT 토큰의 유효성을 검증하는 메소드
    // token은 클라이언트가 전송한 JWT이다.
    public boolean validateToken(String token) {
        try {
            // JWT를 파싱하여 유효성을 검증한다. 서명을 확인하고, 클레임을 파싱한다.
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            log.info("validateToken : " + token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException |
                 MalformedJwtException e) { // 잘못된 JWT 서명인 경우
            log.error("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) { // 만료된 JWT인 경우
//            log.error("Expired JWT Token", e);
            log.error("Expired JWT Token");
        } catch (UnsupportedJwtException e) { // 지원되지 않는 JWT인 경우
            log.error("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) { // JWT 클레임 문자열이 비어있는 경우
            log.error("JWT claims string is empty.", e);
        } catch (Exception e) {
            log.error("validateToken에서 일어난 그 외 무언가 에러");
        }
        return false;
    }

    // JWT 토큰의 클레임을 파싱하는 메소드
    private Claims parseClaims(String accessToken) {
        try {
            // JWT를 파싱하여 클레임(Claims) 정보를 추출한다.
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) { // 토큰이 만료된 경우
            return e.getClaims();
        }
    }

    // Redis에 JWT 토큰을 저장하는 메소드
    public void storeTokenInRedis(Long userId, String accessToken, long expirationTime) {
        // Redis에 저장할 키를 "user:userId" 형식으로 설정
        String redisKey = "user:" + userId;
        redisTemplate.opsForValue().set(redisKey, accessToken, expirationTime, TimeUnit.SECONDS);
        log.info("Access Token 저장됨: userId = {}, token = {}", userId, accessToken);
    }

    // Redis에서 토큰을 조회하는 메소드
    public String getTokenFromRedis(Long userId) {
        String redisKey = "user:" + userId;
        String token = redisTemplate.opsForValue().get(redisKey);
        log.info("Redis에서 가져온 Access Token: userId = {}, token = {}", userId, token);
        return token;
    }

    // JWT 토큰에서 userId를 추출하는 메소드
    public Long getUserIdFromToken(String token) {
        // Bearer 접두어가 있는지 확인하고, 있으면 제거하여 순수한 토큰을 추출
        String resolvedToken = resolveToken(token);

        Claims claims = parseClaims(resolvedToken); // JWT의 클레임을 파싱
        log.info(claims.getSubject());
        return Long.parseLong(claims.getSubject()); // 토큰의 주체(Subject)인 userId를 반환
    }

    // Bearer 접두어를 확인하고 제거하는 메소드
    private String resolveToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            // Bearer 접두어가 있는 경우 접두어 제거 후 반환
            return token.substring(7); // "Bearer "를 제거
        }
        // Bearer 접두어가 없는 경우, 그대로 반환
        return token;
    }
}
