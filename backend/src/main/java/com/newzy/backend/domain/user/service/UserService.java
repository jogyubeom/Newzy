package com.newzy.backend.domain.user.service;

import com.newzy.backend.domain.newzy.dto.request.NewzyListGetRequestDTO;
import com.newzy.backend.domain.user.dto.request.AuthRequestDTO;
import com.newzy.backend.domain.user.dto.request.UserInfoRequestDTO;
import com.newzy.backend.domain.user.dto.request.UserUpdateRequestDTO;
import com.newzy.backend.domain.user.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService {
    void save(UserInfoRequestDTO requestDTO);

    UserUpdateResponseDTO updateUser(String token, UserUpdateRequestDTO request);

    void deleteUser(String token);

    boolean checkUserNickname(String nickname);

    UserInfoResponseDTO getUser(String token);

    UserInfoResponseDTO getUserByNickname(String nickname);

    UserInfoResponseDTO getUserByEmail(String email);

    UserInfoResponseDTO oauthLogin(AuthRequestDTO authRequestDTO);

    UserInfoResponseDTO oauthSignup(AuthRequestDTO authRequestDTO);

    void userSignOut(String token);

    UserFirstLoginResponseDTO isFirstLogin(String token);

    UserInfoResponseDTO updateProfileImage(String token, MultipartFile[] profile);

    int getClusterId(Long userId);

    void followUser(Long userId, String nickname);

    void deleteFollower(Long userId, String nickname);

    Map<String, Object> getFollowingList(int page, String nickname);

    Map<String, Object> getNewsBookmarkList(int page, Long userId);

    Map<String, Object> getNewsLikeList(int page, Long userId);

    Map<String, Object> getNewzyBookmarkList(int page, Long userId);

    Map<String, Object> getNewzyLikeList(int page, Long userId);

    Map<String, Object> getFollowerList(int page, String nickname);

    Map<String, Object> getNewzyListByNickname(int page, String nickname);

    Map<String, Object> getFollowingsNewzyList(NewzyListGetRequestDTO requestDTO, Long userId);

    UserCardCollectorResponseDTO getBestCardCollector();

    List<UserNewporterResponseDTO> getNewporter();
}
