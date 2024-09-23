package com.newzy.backend.domain.newzy.service;

import com.newzy.backend.domain.newzy.dto.request.NewzyRequestDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyResponseDTO;

import java.util.List;

public interface NewzyService {
    void save(NewzyRequestDTO dto);

    NewzyResponseDTO update(Long newzyId, NewzyRequestDTO dto);

    List<NewzyResponseDTO> findAllNewzies();

    void delete(Long newzyId);



}
