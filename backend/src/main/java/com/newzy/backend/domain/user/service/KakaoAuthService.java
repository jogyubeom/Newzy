package com.newzy.backend.domain.user.service;

import com.newzy.backend.domain.user.dto.request.AuthRequestDTO;
import com.newzy.backend.domain.user.entity.User;
import com.newzy.backend.domain.user.repository.UserRepository;
import com.newzy.backend.global.auth.JwtProvider;
import com.newzy.backend.global.util.RandomStringGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j // 로그를 사용하기 위해 추가
public class KakaoAuthService implements AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;


    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    // Kakao 로그인 또는 회원가입 처리
    @Override
    public String handleLoginOrSignup(String code) {
        log.info("Handling login or signup with Kakao for authorization code: {}", code);

        // 1. 받은 code로 access token 요청
        String accessToken = getKakaoAccessToken(code);
        log.info("Access token received from Kakao: {}", accessToken);

        // 2. access token을 사용하여 Kakao 사용자 정보 가져오기
        AuthRequestDTO userInfo = getKakaoUserInfo(accessToken);
        log.info("User information from Kakao: email={}, nickname={}", userInfo.getEmail(), userInfo.getNickname());

        // 3. 사용자 정보로 회원가입 또는 로그인 처리
        Optional<User> existingUser = userRepository.findUserByEmailAndSocialLoginType(
                userInfo.getEmail(), userInfo.getType());

        if (existingUser.isPresent()) {
            // 기존 사용자가 있으면 JWT 토큰 발급 (로그인)
            String jwtToken = jwtProvider.generateToken(existingUser.get()).getAccessToken();
            log.info("Existing user found. JWT token issued: {}", jwtToken);
            // redis에 저장
            jwtProvider.storeTokenInRedis(existingUser.get().getUserId(), jwtToken, EXPIRATION_TIME);
            return jwtToken;
        } else {
            // 기존 사용자가 없으면 회원가입 처리
            log.info("No existing user found. Proceeding with signup.");
            AuthRequestDTO authRequestDTO = AuthRequestDTO.builder()
                    .email(userInfo.getEmail())
                    .nickname(userInfo.getNickname())
                    .password(new BCryptPasswordEncoder().encode(RandomStringGenerator.generateRandomString(10)))
                    .type("kakao")
                    .build();

            userService.oauthSignup(authRequestDTO);
            log.info("User successfully signed up with Kakao.");

            // 회원가입 성공 후, 'signup' 반환
            return "signup";
        }
    }

    @Override
    // 리다이렉트 URL 생성 메소드
    public String generateLoginUrl() {
        String loginUrl = "https://kauth.kakao.com/oauth/authorize?client_id=" + kakaoClientId +
                "&redirect_uri=" + kakaoRedirectUri +
                "&response_type=code";
        log.info("Generated Kakao login URL: {}", loginUrl);
        return loginUrl;
    }

    // Kakao Access Token 요청
    private String getKakaoAccessToken(String code) {
        log.info("Requesting access token from Kakao for authorization code: {}", code);

        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("client_secret", kakaoClientSecret);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        String accessToken = response.getBody().get("access_token").toString();
        log.info("Access token received from Kakao: {}", accessToken);

        return accessToken;
    }

    // Kakao 사용자 정보 요청
    private AuthRequestDTO getKakaoUserInfo(String accessToken) {
        log.info("Requesting Kakao user information using access token: {}", accessToken);

        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, Map.class);

        Map<String, Object> kakaoAccount = (Map<String, Object>) response.getBody().get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        AuthRequestDTO authRequestDTO = AuthRequestDTO.builder()
                .email(kakaoAccount.get("email").toString())
                .nickname(profile.get("nickname").toString())
                .type("kakao")
                .build();
        log.info("Kakao user information retrieved: email={}, nickname={}",
                authRequestDTO.getEmail(), authRequestDTO.getNickname());

        return authRequestDTO;
    }
}