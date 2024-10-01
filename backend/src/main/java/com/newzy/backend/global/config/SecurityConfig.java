package com.newzy.backend.global.config;

import com.newzy.backend.global.auth.JwtAuthenticationEntryPoint;
import com.newzy.backend.global.auth.JwtAuthenticationFilter;
import com.newzy.backend.global.auth.JwtProvider;
import com.newzy.backend.global.auth.OAuth2UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration // 이 클래스는 Spring의 설정 클래스로 사용
@EnableWebSecurity
// Spring Security를 활성화합니다. 이 어노테이션은 웹 보안 기능을 추가하고, 기본 보안 설정을 적용
@RequiredArgsConstructor
// final로 선언된 필드에 대한 생성자를 자동으로 생성. JwtProvider 주입을 위해 사용
public class SecurityConfig {
    // 이 친구는 JWT 토큰의 생성 및 검증을 담당
    private final JwtProvider jwtProvider;
    private final OAuth2UserServiceImpl oAuth2UserService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    // 이 메소드가 반환하는 객체를 빈으로 등록. filterChain 메소드는 Spring Security의 주요 설정을 구성한다. HttpSecurity 객체로 다양한 보안 설정이 가능하다.
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(httpSecurityHttpBasicConfigurer -> { // httpBasic은 기본인증 설정이다.
                    httpSecurityHttpBasicConfigurer.disable(); // HTTP 기본 인증을 비활성화한다. 이는 브라우저의 기본 인증 팝업을 비활성화하는 데 사용된다.
                })
                .csrf(httpSecurityCsrfConfigurer -> { // CSRF(Cross-Site Request Forgery) 보호에 관한 설정이다.
                    httpSecurityCsrfConfigurer.disable(); // RESTful API를 사용하는 경우, 일반적으로 세션을 사용하지 않으므로 CSRF 보호를 비활성화한다.
                })
                .sessionManagement(httpSecuritySessionManagementConfigurer -> { // 세션 관리 설정이다.
                    // 세션을 사용하지 않도록 설정한다. 모든 요청은 상태 없이 처리되며, JWT를 통해 인증을 관리하도록 설정한다.
                    httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> { // HTTP 요청에 대한 접근 권한을 설정한다.
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers("/**").permitAll() // 모든 사용자 가능(개발중 혹은 공용 API에 적용)
//                            .requestMatchers(HttpMethod.GET, "/api/files/**").permitAll()
//                            .requestMatchers(HttpMethod.POST, "/api/login", "/api/member/signup").permitAll()
//                            .requestMatchers(HttpMethod.POST, "/api/member/findpassword").permitAll()
//                            .requestMatchers(HttpMethod.POST, "/api/member/oauth2/kakao").permitAll()
//                            .requestMatchers(HttpMethod.POST, "/api/member/updatepassword").permitAll()
//                            .requestMatchers(HttpMethod.GET, "/api/review").permitAll()
                            .anyRequest().authenticated(); // 나머지 모든 요청에 대해 인증을 요구
                })
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)) // OAuth2 사용자 정보 처리 서비스
                        .successHandler((request, response, authentication) -> {
                            // 로그인 성공 처리, 여기서 JWT 토큰이 OAuth2UserServiceImpl에서 발급됨
                            response.setStatus(HttpServletResponse.SC_OK);
                        })
                        .failureHandler((request, response, exception) -> {
                            // 로그인 실패 처리
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "OAuth2 authentication failed");
                        })
                )
                // UsernamePasswordAuthenticationFilter(사용자 이름과 비밀번호를 통한 기본 인증 필터) 전에 JWT 인증을 처리하는 JwtAuthenticationFilter((user-defined) 필터 추가
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                );

        return http.build();
    }

    /**
     * 비밀번호 암호화 알고리즘 설정
     */
    @Bean
    public PasswordEncoder passwordEncoder() { // 비밀번호를 암호화하는 PasswordEncoder 빈을 정의
        // Crypt 해시 함수를 사용하여 비밀번호를 암호화하는 BCryptPasswordEncoder 객체를 반환한다. 이는 비밀번호를 안전하게 저장하고, 인증 시 비교하는 데 사용된다.
        return new BCryptPasswordEncoder();
    }

    // 추가된 CORS 설정
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("https://j11b305.p.ssafy.io");
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("https://plogbucket.s3.ap-northeast-2.amazonaws.com/**");
        config.addAllowedOriginPattern("https://plogbucket.s3.ap-northeast-2.amazonaws.com/**");
        config.addAllowedOriginPattern("http://localhost:3000");
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}