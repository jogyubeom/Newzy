package com.newzy.backend.domain.user.service;

import com.newzy.backend.domain.newzy.dto.response.NewzyResponseDTO;
import com.newzy.backend.domain.user.dto.request.UserInfoRequestDTO;
import com.newzy.backend.domain.user.dto.response.UserInfoResponseDTO;
import com.newzy.backend.domain.user.entity.User;
import com.newzy.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
}
