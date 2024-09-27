package com.newzy.backend.domain.newzy.service;

import com.newzy.backend.domain.newzy.dto.request.NewzyRequestDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyListGetResponseDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NewzyService {
    void save(NewzyRequestDTO dto);

    NewzyResponseDTO update(Long newzyId, NewzyRequestDTO dto);

    List<NewzyListGetResponseDTO> getNewzyList(int page, int category);

    NewzyResponseDTO getNewzyDetail(Long newzyId);

    void delete(Long newzyId);

    void bookmark(Long newzyId);

    void deleteBookmark(Long bookmarkId);

    void likeNewzy(Long newzyId);

    void deleteLike(Long newzyLikeId);

}
