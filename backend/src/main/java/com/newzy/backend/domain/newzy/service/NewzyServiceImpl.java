package com.newzy.backend.domain.newzy.service;

import com.newzy.backend.domain.newzy.dto.request.NewzyRequestDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyResponseDTO;
import com.newzy.backend.domain.newzy.entity.Category;
import com.newzy.backend.domain.newzy.entity.Newzy;
import com.newzy.backend.domain.newzy.repository.NewzyRepository;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NewzyServiceImpl implements NewzyService {

    private final NewzyRepository newzyRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<NewzyResponseDTO> getNewzyList(int page, Category category) {
        log.info(">>> newzyServiceImpl getNewzyList - pages: {}. category: {}", page, category);
        Pageable pageable = PageRequest.of(page, 10);
        Page<Newzy> newzies = newzyRepository.findByCategoryAndIsDeletedFalse(category, pageable);

        return newzies.map(NewzyResponseDTO::convertToDTO);
    }

    @Override
    public void save(NewzyRequestDTO dto) {
        Newzy newzy = Newzy.convertToEntity(dto);
         newzyRepository.save(newzy);
    }

    @Override
    public NewzyResponseDTO update(Long newzyId, NewzyRequestDTO dto) {
        Newzy updatedNewzy =  Newzy.convertToEntity(newzyId, dto);
        Newzy newzy = newzyRepository.updateNewzyInfo(updatedNewzy);
        NewzyResponseDTO newzyResponseDTO = NewzyResponseDTO.convertToDTO(newzy);

        return newzyResponseDTO;
    }

    @Override
    public void delete(Long newzyId) {
        newzyRepository.deleteNewzyById(newzyId);
    }

//    @Override
//    public void bookmark(Long newzyId, NewzyRequestDTO dto) {
//
//    }

}
