package com.newzy.backend.domain.user.controller;

import com.newzy.backend.domain.user.service.GoogleAuthService;
import com.newzy.backend.domain.user.service.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
@Slf4j // 로그를 사용하기 위해 추가
public class AuthController {

    private final KakaoAuthService kakaoAuthService;
    private final GoogleAuthService googleAuthService;

    // Kakao OAuth2 로그인 리다이렉트 URL 제공
    @GetMapping("/kakao/authorize")
    public ResponseEntity<?> getKakaoAuthorizeUrl() {
        // 서비스에서 Kakao 로그인 URL 생성
        log.info("Generating Kakao login URL");
        String kakaoAuthorizeUrl = kakaoAuthService.generateLoginUrl();
        log.info("Kakao login URL: {}", kakaoAuthorizeUrl);
        return ResponseEntity.ok().body(kakaoAuthorizeUrl);
    }

    // Kakao 로그인 콜백 처리
    @GetMapping("/code/kakao")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code) {
        try {
            log.info("Received Kakao authorization code: {}", code);
            // Kakao 로그인/회원가입 처리
            String result = kakaoAuthService.handleLoginOrSignup(code);

            if (result.equals("signup")) {
                // 회원가입 성공
                log.info("Kakao signup successful");
                return ResponseEntity.ok().body("Signup success");
            } else {
                // 로그인 성공 (JWT 토큰 반환)
                log.info("Kakao login successful, token issued: {}", result);
                return ResponseEntity.ok().header("Authorization", "Bearer " + result).body("Login success");
            }
        } catch (Exception e) {
            log.error("Kakao OAuth2 authentication failed", e);
            return ResponseEntity.status(401).body("OAuth2 authentication failed");
        }
    }

    // Google OAuth2 로그인 리다이렉트 URL 제공
    @GetMapping("/google/authorize")
    public ResponseEntity<?> getGoogleAuthorizeUrl() {
        log.info("Generating Google login URL");
        String googleAuthorizeUrl = googleAuthService.generateLoginUrl();
        log.info("Google login URL: {}", googleAuthorizeUrl);
        return ResponseEntity.ok().body(googleAuthorizeUrl);
    }

    // Google 로그인 콜백 처리
    @GetMapping("/code/google")
    public ResponseEntity<?> googleCallback(@RequestParam String code) {
        try {
            log.info("Received Google authorization code: {}", code);
            // Kakao 로그인/회원가입 처리
            String result = googleAuthService.handleLoginOrSignup(code);

            if (result.equals("signup")) {
                // 회원가입 성공
                log.info("Google signup successful");
                return ResponseEntity.ok().body("Signup success");
            } else {
                // 로그인 성공 (JWT 토큰 반환)
                log.info("Google login successful, token issued: {}", result);
                return ResponseEntity.ok().header("Authorization", "Bearer " + result).body("Login success");
            }
        } catch (Exception e) {
            log.error("Kakao OAuth2 authentication failed", e);
            return ResponseEntity.status(401).body("OAuth2 authentication failed");
        }
    }
}
