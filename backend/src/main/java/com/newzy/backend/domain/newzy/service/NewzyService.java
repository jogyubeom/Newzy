package com.newzy.backend.domain.newzy.service;

import com.newzy.backend.domain.newzy.dto.request.NewzyRequestDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyResponseDTO;
import com.newzy.backend.domain.newzy.entity.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NewzyService {
    void save(NewzyRequestDTO dto);

    NewzyResponseDTO update(Long newzyId, NewzyRequestDTO dto);

    Page<NewzyResponseDTO> getNewzyList(int page, Category category);

    void delete(Long newzyId);

//    void bookmark(Long newzyId, NewzyRequestDTO dto);

}
