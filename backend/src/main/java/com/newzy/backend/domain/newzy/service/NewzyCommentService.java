package com.newzy.backend.domain.newzy.service;

import com.newzy.backend.domain.newzy.dto.request.NewzyCommentRequestDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyCommentResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NewzyCommentService {

    void saveComment(Long newzyId, NewzyCommentRequestDTO requestDTO);

    NewzyCommentResponseDTO updateComment(Long newzyCommentId, NewzyCommentRequestDTO requestDTO);

    Page<NewzyCommentResponseDTO> getNewzyCommentListByNewzyId(Long newzyId, int page, int size);

    void deleteComment(Long newzyCommentId);

}
