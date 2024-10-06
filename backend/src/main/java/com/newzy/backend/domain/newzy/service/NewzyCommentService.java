package com.newzy.backend.domain.newzy.service;

import com.newzy.backend.domain.newzy.dto.request.NewzyCommentRequestDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyCommentListGetResponseDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyCommentResponseDTO;

import java.util.List;

public interface NewzyCommentService {

    void saveComment(Long userId, Long newzyId, NewzyCommentRequestDTO requestDTO);

    NewzyCommentResponseDTO updateComment(Long userId, Long newzyCommentId, NewzyCommentRequestDTO requestDTO);

    void deleteComment(Long userId, Long newzyCommentId);

    List<NewzyCommentListGetResponseDTO> getNewzyCommentList(Long newzyId);
}
