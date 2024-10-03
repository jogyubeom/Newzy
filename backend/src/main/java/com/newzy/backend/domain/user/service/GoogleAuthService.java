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
@Slf4j
public class GoogleAuthService implements AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    // Google 로그인 또는 회원가입 처리
    @Override
    public String handleLoginOrSignup(String code) {
        log.info("Handling login or signup with Google for authorization code: {}", code);

        // 1. 받은 code로 access token 요청
        String accessToken = getGoogleAccessToken(code);
        log.info("Access token received from Google: {}", accessToken);

        // 2. access token을 사용하여 Google 사용자 정보 가져오기
        AuthRequestDTO userInfo = getGoogleUserInfo(accessToken);
        log.info("User information from Google: email={}, name={}", userInfo.getEmail(), userInfo.getNickname());

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
                    .type("google")
                    .build();

            userService.oauthSignup(authRequestDTO);
            log.info("User successfully signed up with Google.");

            // 회원가입 성공 후, 'signup' 반환
            return "signup";
        }
    }

    @Override
    // 리다이렉트 URL 생성 메소드
    public String generateLoginUrl() {
        String loginUrl = "https://accounts.google.com/o/oauth2/auth?client_id=" + googleClientId +
                "&redirect_uri=" + googleRedirectUri +
                "&response_type=code" +
                "&scope=email%20profile";
        log.info("Generated Google login URL: {}", loginUrl);
        return loginUrl;
    }

    // Google Access Token 요청
    private String getGoogleAccessToken(String code) {
        log.info("Requesting access token from Google for authorization code: {}", code);

        String tokenUrl = "https://oauth2.googleapis.com/token";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", googleRedirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        String accessToken = response.getBody().get("access_token").toString();
        log.info("Access token received from Google: {}", accessToken);

        return accessToken;
    }

    // Google 사용자 정보 요청
    private AuthRequestDTO getGoogleUserInfo(String accessToken) {
        log.info("Requesting Google user information using access token: {}", accessToken);

        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, Map.class);

        String email = response.getBody().get("email").toString();
        String name = response.getBody().get("name").toString();

        AuthRequestDTO googleRequestDTO =
                AuthRequestDTO.builder()
                        .email(email)
                        .nickname(name)
                        .type("google")
                        .build();
        log.info("Google user information retrieved: email={}, name={}", googleRequestDTO.getEmail(), googleRequestDTO.getNickname());

        return googleRequestDTO;
    }
}
