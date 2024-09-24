package com.newzy.backend.domain.newzy.service;

import com.newzy.backend.domain.newzy.dto.request.NewzyCommentRequestDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyCommentResponseDTO;
import com.newzy.backend.domain.newzy.entity.Newzy;
import com.newzy.backend.domain.newzy.entity.NewzyComment;
import com.newzy.backend.domain.newzy.repository.NewzyCommentRepository;
import com.newzy.backend.domain.newzy.repository.NewzyRepository;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@ToString
public class NewzyCommentServiceImpl implements NewzyCommentService {

    private final NewzyCommentRepository newzyCommentRepository;
    private final NewzyRepository newzyRepository;

    @Override
    @Transactional(readOnly = true)
    public List<NewzyCommentResponseDTO> findAllCommentsByNewzyId(Long newzyId) {
        List<NewzyComment> newzyComments = newzyCommentRepository.findAllByNewzy_NewzyIdAndIsDeletedFalse(newzyId);
        List<NewzyCommentResponseDTO> newzyCommentsResponseDTO = new ArrayList<>();

        for (NewzyComment newzyComment : newzyComments) {
            NewzyCommentResponseDTO responseDTO = NewzyCommentResponseDTO.convertToDTO(newzyComment);
            if (! newzyComment.getIsDeleted()) {
                newzyCommentsResponseDTO.add(responseDTO);
            }
        }

        return newzyCommentsResponseDTO;
    }

    @Override
    public void saveComment(Long newzyId, NewzyCommentRequestDTO dto){
        Newzy newzy = newzyRepository.findById(newzyId).orElseThrow(() -> new IllegalStateException("Newzy not found with ID: " + newzyId));

        NewzyComment newzyComment = NewzyComment.convertToEntityByNewzyId(dto, newzy);

        newzyCommentRepository.save(newzyComment);
    }

    @Override
    public NewzyCommentResponseDTO updateComment(Long newzyCommentId, NewzyCommentRequestDTO dto) {
        NewzyComment updatedNewzyComment = NewzyComment.convertToEntityByNewzyCommentId(newzyCommentId, dto);
        NewzyComment newzyComment = newzyCommentRepository.updateNewzyCommentById(updatedNewzyComment);
        NewzyCommentResponseDTO commentResponseDTO = NewzyCommentResponseDTO.convertToDTO(newzyComment);

        return commentResponseDTO;
    }

    @Override
    public void deleteComment(Long newzyCommentId) {
        newzyCommentRepository.deleteNewzyCommentById(newzyCommentId);
    }

}