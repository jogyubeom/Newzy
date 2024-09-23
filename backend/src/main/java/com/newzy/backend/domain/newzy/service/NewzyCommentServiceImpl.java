package com.newzy.backend.domain.newzy.service;

import com.newzy.backend.domain.newzy.dto.request.NewzyCommentRequestDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyCommentResponseDTO;
import com.newzy.backend.domain.newzy.entity.Newzy;
import com.newzy.backend.domain.newzy.entity.NewzyComment;
import com.newzy.backend.domain.newzy.repository.NewzyCommentRepository;
import com.newzy.backend.domain.newzy.repository.NewzyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewzyCommentServiceImpl implements NewzyCommentService {
    private final NewzyCommentRepository newzyCommentRepository;

    private NewzyComment convertToEntity( NewzyCommentRequestDTO requestDTO){
        NewzyComment newzyComment = new NewzyComment();
        newzyComment.setNewzyComment(requestDTO.getNewzyComment());

        return newzyComment;
    }

    private NewzyComment convertToEntity(Long newzyCommentId, NewzyCommentRequestDTO requestDTO){
        NewzyComment newzyComment = new NewzyComment();
        newzyComment.setNewzyCommentId(newzyCommentId);
        newzyComment.setNewzyComment(requestDTO.getNewzyComment());

        return newzyComment;
    }

    private NewzyCommentResponseDTO convertToDTO(NewzyComment newzyComment){
        if(newzyComment == null){ return null; }

        return new NewzyCommentResponseDTO(newzyComment.getNewzyCommentId(), newzyComment.getNewzyComment());
    }

    @Override
    @Transactional
    public void saveComment(NewzyCommentRequestDTO dto){
        NewzyComment newzyComment = convertToEntity(dto);
        newzyCommentRepository.save(newzyComment);
    }

    @Override
    @Transactional
    public NewzyCommentResponseDTO updateComment(Long newzyCommentId, NewzyCommentRequestDTO dto) {
        NewzyComment updatedNewzyComment = convertToEntity(newzyCommentId, dto);
        NewzyComment newzyComment = newzyCommentRepository.updateNewzyCommentById(updatedNewzyComment);
        NewzyCommentResponseDTO commentResponseDTO = convertToDTO(newzyComment);

        return commentResponseDTO;
    }

    @Override
    public List<NewzyCommentResponseDTO> findAllCommentsByNewzyId(Long newzyId) {
        List<NewzyComment> newzyComments = newzyCommentRepository.findAll();
        List<NewzyCommentResponseDTO> newzyCommentsResponseDTO = new ArrayList<>();

        for (NewzyComment newzyComment : newzyComments) {
            NewzyCommentResponseDTO responseDTO = convertToDTO(newzyComment);
            if(! newzyComment.getIsDeleted()){
                newzyCommentsResponseDTO.add(responseDTO);
            }
        }

        return newzyCommentsResponseDTO;
    }

    @Override
    public void deleteComment(Long newzyCommentId) {
        newzyCommentRepository.deleteNewzyCommentById( newzyCommentId);
    }


}

























