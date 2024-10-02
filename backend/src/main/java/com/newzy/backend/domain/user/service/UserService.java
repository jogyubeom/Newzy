package com.newzy.backend.domain.user.service;

import com.newzy.backend.domain.newzy.dto.response.NewzyResponseDTO;
import com.newzy.backend.domain.user.dto.request.AuthRequestDTO;
import com.newzy.backend.domain.user.dto.request.UserInfoRequestDTO;
import com.newzy.backend.domain.user.dto.response.UserInfoResponseDTO;

import java.util.List;

public interface UserService {
    void save(UserInfoRequestDTO requestDTO);

    NewzyResponseDTO updateUserInfo(Long id, UserInfoRequestDTO requestDTO);

    void deleteUserInfo(Long userId);

    List<NewzyResponseDTO> getAllUsers();

    NewzyResponseDTO getUserById(Long id);

    UserInfoResponseDTO getUser(String token);

    AuthRequestDTO oauthLogin(AuthRequestDTO authRequestDTO);

}
