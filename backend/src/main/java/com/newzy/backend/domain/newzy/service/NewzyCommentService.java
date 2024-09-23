package com.newzy.backend.domain.newzy.service;

import com.newzy.backend.domain.newzy.dto.request.NewzyCommentRequestDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyCommentResponseDTO;

import java.util.List;

public interface NewzyCommentService {

    void saveComment(NewzyCommentRequestDTO requestDTO);

    NewzyCommentResponseDTO updateComment(Long newzyCommentId, NewzyCommentRequestDTO requestDTO);

    List<NewzyCommentResponseDTO> findAllCommentsByNewzyId(Long newzyId);

    void deleteComment(Long newzyCommentId);

}
