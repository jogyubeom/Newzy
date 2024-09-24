package com.newzy.backend.domain.newzy.service;

import com.newzy.backend.domain.newzy.dto.request.NewzyRequestDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyResponseDTO;
import com.newzy.backend.domain.newzy.entity.Newzy;
import com.newzy.backend.domain.newzy.repository.NewzyRepository;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewzyServiceImpl implements NewzyService {

    private final NewzyRepository newzyRepository;

    @Override
    public List<NewzyResponseDTO> findAllNewzies() {
        List<Newzy> newzies = newzyRepository.findAll();
        List<NewzyResponseDTO> newziesResponseDTO = new ArrayList<>();

        for (Newzy newzy : newzies){
            NewzyResponseDTO dto = NewzyResponseDTO.convertToDTO(newzy);
            if (! newzy.isDeleted()){   newziesResponseDTO.add(dto);    }
        }

        return newziesResponseDTO;
    }

    @Override
    @Transactional
    public void save(NewzyRequestDTO dto) {
        Newzy newzy = Newzy.convertToEntity(dto);
         newzyRepository.save(newzy);
    }

    @Override
    @Transactional
    public NewzyResponseDTO update(Long newzyId, NewzyRequestDTO dto) {
        Newzy updatedNewzy =  Newzy.convertToEntity(newzyId, dto);
        Newzy newzy = newzyRepository.updateNewzyInfo(updatedNewzy);
        NewzyResponseDTO newzyResponseDTO = NewzyResponseDTO.convertToDTO(newzy);

        return newzyResponseDTO;
    }

    @Override
    @Transactional
    public void delete(Long newzyId) {
        newzyRepository.deleteNewzyById(newzyId);
    }
}
