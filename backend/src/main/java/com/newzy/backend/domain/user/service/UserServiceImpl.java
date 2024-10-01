package com.newzy.backend.domain.user.service;

import com.newzy.backend.domain.newzy.dto.response.NewzyResponseDTO;
import com.newzy.backend.domain.user.dto.request.AuthRequestDTO;
import com.newzy.backend.domain.user.dto.request.UserInfoRequestDTO;
import com.newzy.backend.domain.user.dto.response.UserInfoResponseDTO;
import com.newzy.backend.domain.user.entity.User;
import com.newzy.backend.domain.user.repository.UserRepository;
import com.newzy.backend.global.auth.JwtProvider;
import com.newzy.backend.global.exception.NotValidRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public void save(UserInfoRequestDTO requestDTO) {
        User user = User.convertToEntity(requestDTO);
        userRepository.save(user);
    }

    @Override
    public NewzyResponseDTO updateUserInfo(Long id, UserInfoRequestDTO requestDTO) {
        User updatedUserInfo = User.convertToEntity(id, requestDTO);
        User user = userRepository.updateUserInfo(updatedUserInfo);
        UserInfoResponseDTO responseDTO = UserInfoResponseDTO.convertToDTO(user);

        return null;
    }

    @Override
    public void deleteUserInfo(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewzyResponseDTO> getAllUsers() {
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public NewzyResponseDTO getUserById(Long id) {
        return null;
    }

    @Override
    public UserInfoResponseDTO getUser(String token) {
        // 1. 토큰에서 사용자 ID 추출
        Long userId;
        try {
            userId = jwtProvider.getUserIdFromToken(token); // JWT 토큰에서 userId 추출
            log.info(">>> getUser - 추출된 사용자 ID: {}", userId);
        } catch (Exception e) {
            log.error(">>> getUser - 토큰으로부터 사용자 ID를 추출하는 데 실패함: {}", e.getMessage());
            throw new NotValidRequestException("유효하지 않은 토큰입니다.");
        }

        // 2. 사용자 조회
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error(">>> getUser - 사용자를 찾을 수 없음: {}", userId);
            throw new NotValidRequestException("사용자를 찾을 수 없습니다.");
        });

        log.info(">>> getUser - 사용자 정보: {}", user);

        // 3. 사용자 정보 DTO 반환
        return UserInfoResponseDTO.convertToDTO(user);
    }

    @Override
    public AuthRequestDTO oauthLogin(AuthRequestDTO authRequestDTO) {
        try {
            // 이메일로 기존 사용자 검색
            Optional<User> existingUser = userRepository.findUserByEmail(authRequestDTO.getEmail());

            if (existingUser.isPresent()) {
                log.info("기존 사용자 로그인: {}", existingUser.get().getEmail());
                // 기존 사용자 처리만 하고 반환값 없음
                return AuthRequestDTO.builder()
                        .email(existingUser.get().getEmail())
                        .nickname(existingUser.get().getNickname())
                        .password(existingUser.get().getPassword())
                        .build();
            } else {
                // 새로운 사용자 등록
                User user = User.builder()
                        .email(authRequestDTO.getEmail())
                        .nickname(authRequestDTO.getNickname())
                        .password(authRequestDTO.getPassword())
                        .build();
                log.info("새로운 사용자 등록: {}", authRequestDTO.getEmail());
                userRepository.save(user); // 새 사용자 저장
                return null;
            }
        } catch (Exception e) {
            // 예외 발생 시 로그를 남기고, 예외 처리
            log.error("사용자 처리 중 예외 발생: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "사용자 처리 중 문제가 발생했습니다.");
        }
    }


}