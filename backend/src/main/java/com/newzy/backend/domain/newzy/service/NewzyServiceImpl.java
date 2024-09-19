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

    private Newzy convertToEntity(NewzyRequestDTO dto){
        Newzy newzy = new Newzy();
        newzy.setTitle(dto.getTitle());
        newzy.setContent(dto.getContent());
        newzy.setCategory(dto.getCategory());

        return newzy;
    }

    private Newzy convertToEntity(Long newzyId , NewzyRequestDTO dto){
        Newzy newzy = new Newzy();
        newzy.setNewzyId(newzyId);
        newzy.setTitle(dto.getTitle());
        newzy.setContent(dto.getContent());
        newzy.setCategory(dto.getCategory());

        return newzy;
    }

    private NewzyResponseDTO convertToDTO(Newzy newzy){
        if (newzy == null){
            return null;
        }

        return new NewzyResponseDTO(newzy.getNewzyId(), newzy.getTitle(), newzy.getContent(), newzy.getCategory());
    }

    @Override
    @Transactional
    public void save(NewzyRequestDTO dto) {
        Newzy newzy = convertToEntity(dto);
         newzyRepository.save(newzy);
    }

    @Override
    @Transactional
    public NewzyResponseDTO update(Long newzyId, NewzyRequestDTO dto) {
        Newzy updatedNewzy = convertToEntity(newzyId, dto);
        Newzy newzy = newzyRepository.updateInfo(updatedNewzy);
        NewzyResponseDTO newzyResponseDTO = convertToDTO(newzy);

        return newzyResponseDTO;
    }

    @Override
    public List<NewzyResponseDTO> findNewzies() {
        List<Newzy> newzies = newzyRepository.findAll();
        List<NewzyResponseDTO> newziesResponseDTO = new ArrayList<>();

        for (Newzy newzy : newzies){
            NewzyResponseDTO dto = convertToDTO(newzy);
            if (! newzy.getIsDeleted()){
                newziesResponseDTO.add(dto);
            }
        }

        return newziesResponseDTO;
    }

    @Override
    public void delete(Long newzyId) {
        newzyRepository.deleteById(newzyId);
    }
}
