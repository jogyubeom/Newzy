package com.newzy.backend.domain.user.service;

import com.newzy.backend.domain.newzy.dto.response.NewzyResponseDTO;
import com.newzy.backend.domain.user.dto.request.AuthRequestDTO;
import com.newzy.backend.domain.user.dto.request.UserInfoRequestDTO;
import com.newzy.backend.domain.user.dto.request.UserUpdateRequestDTO;
import com.newzy.backend.domain.user.dto.response.UserFirstLoginResponseDTO;
import com.newzy.backend.domain.user.dto.response.UserInfoResponseDTO;
import com.newzy.backend.domain.user.dto.response.UserUpdateResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    void save(UserInfoRequestDTO requestDTO);

    UserUpdateResponseDTO updateUser(String token, UserUpdateRequestDTO request);


    void deleteUser(String token);

    List<NewzyResponseDTO> getAllUsers();

    NewzyResponseDTO getUserById(Long id);

    UserInfoResponseDTO getUser(String token);

    UserInfoResponseDTO getUserByEmail(String email);

    UserInfoResponseDTO oauthLogin(AuthRequestDTO authRequestDTO);

    UserInfoResponseDTO oauthSignup(AuthRequestDTO authRequestDTO);

    void userSignOut(String token);

    UserFirstLoginResponseDTO isFirstLogin(String token);

    UserInfoResponseDTO updateProfileImage(String token, MultipartFile[] profile);

    int getClusterId(Long userId);
}
