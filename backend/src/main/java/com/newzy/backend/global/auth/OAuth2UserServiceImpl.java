package com.newzy.backend.global.auth;

import com.newzy.backend.domain.user.dto.request.AuthRequestDTO;
import com.newzy.backend.domain.user.repository.UserRepository;
import com.newzy.backend.domain.user.service.UserService;
import com.newzy.backend.global.util.RandomStringGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        log.info("OAuth2UserRequest received: {}", userRequest);

        // 소셜 로그인 제공자 (카카오, 구글, 네이버 등)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("OAuth2User loaded: {}", oAuth2User);

        // 사용자 정보 가져오기
        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info("User attributes received: {}", attributes);

//        // 소셜 로그인 제공자에 따라 사용자 정보 처리
//        AuthRequestDTO authRequestDTO;
//        switch (registrationId) {
//            case "kakao":
//                authRequestDTO = getKakaoUser(attributes);
//                break;
//            case "google":
//                authRequestDTO = getGoogleUser(attributes);
//                break;
////            case "naver":
////                authRequestDTO = getNaverUser(attributes);
////                break;
//            default:
//                throw new IllegalArgumentException("Unsupported social login provider: " + registrationId);
//        }
//
//        // 사용자 정보를 UserService로 전달해 새 사용자 등록 또는 기존 사용자 확인
//        authRequestDTO = userService.oauthSignup(authRequestDTO);
//
//        if (authRequestDTO != null) { // 기존 사용자
//            // 기존 사용자 로그인 성공 시 JWT 토큰 발급
//            User user = userRepository.findUserByEmail(authRequestDTO.getEmail())
//                    .orElseThrow(EntityNotFoundException::new);
//            TokenInfo tokenInfo = jwtProvider.generateToken(user);
//
//            // Redis에 토큰 저장 (만료 시간 설정: TOKEN_EXPIRATION_TIME 사용)
//            // 토큰 만료 시간 설정 (예: 1시간)
//            long TOKEN_EXPIRATION_TIME = 3600;
//            jwtProvider.storeTokenInRedis(user.getUserId(), tokenInfo.getAccessToken(), TOKEN_EXPIRATION_TIME);
//
//            // JWT 토큰을 헤더에 포함하여 반환
//            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
//            response.setHeader("Authorization", "Bearer " + tokenInfo.getAccessToken());
//        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName()
        );
    }

    // 카카오 사용자 정보 처리
    private AuthRequestDTO getKakaoUser(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");

        log.info("Kakao user details: email={}, nickname={}", email, nickname);

        return AuthRequestDTO.builder()
                .email(email)
                .nickname(nickname)
                .password(new BCryptPasswordEncoder().encode(RandomStringGenerator.generateRandomString(10)))
                .type("kakao")
                .build();
    }

    // 구글 사용자 정보 처리
    private AuthRequestDTO getGoogleUser(Map<String, Object> attributes) {
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        log.info("Google user details: email={}, name={}", email, name);

        return AuthRequestDTO.builder()
                .email(email)
                .nickname(name)
                .password(new BCryptPasswordEncoder().encode(RandomStringGenerator.generateRandomString(10)))
                .type("google")
                .build();
    }

    // 네이버 사용자 정보 처리
//    private AuthRequestDTO getNaverUser(Map<String, Object> attributes) {
//        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
//        String email = (String) response.get("email");
//        String nickname = (String) response.get("nickname");
//
//        log.info("Naver user details: email={}, nickname={}", email, nickname);
//
//        return AuthRequestDTO.builder()
//                .email(email)
//                .nickname(nickname)
//                .password(new BCryptPasswordEncoder().encode(RandomStringGenerator.generateRandomString(10)))
//                .build();
//    }
}

