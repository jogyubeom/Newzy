package com.newzy.backend.domain.newzy.service;

import com.newzy.backend.domain.newzy.dto.request.NewzyListGetRequestDTO;
import com.newzy.backend.domain.newzy.dto.request.NewzyRequestDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyImageResponseDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyListGetResponseDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface NewzyService {
    void save(Long userId, NewzyRequestDTO dto);

    NewzyResponseDTO update(Long userId, Long newzyId, NewzyRequestDTO dto);

    Map<String, Object> getNewzyList(NewzyListGetRequestDTO requestDTO, Long userId);

    NewzyResponseDTO getNewzyDetail(Long userId, Long newzyId);

    void delete(Long userId, Long newzyId);

    void bookmark(Long userId, Long newzyId);

    void deleteBookmark(Long userId, Long newzyId);

    void likeNewzy(Long userId, Long newzyId);

    void deleteNewzyLike(Long userId, Long newzyId);

    List<NewzyListGetResponseDTO> getHotNewzyList();

    NewzyImageResponseDTO convertImgUrl(MultipartFile[] files);

}
