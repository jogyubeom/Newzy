package com.newzy.backend.domain.newzy.service;

import com.newzy.backend.domain.newzy.dto.request.NewzyCommentRequestDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyCommentListGetResponseDto;
import com.newzy.backend.domain.newzy.dto.response.NewzyCommentResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface NewzyCommentService {

    void saveComment(Long userId, Long newzyId, NewzyCommentRequestDTO requestDTO);

    NewzyCommentResponseDTO updateComment(Long userId, Long newzyCommentId, NewzyCommentRequestDTO requestDTO);

    void deleteComment(Long userId, Long newzyCommentId);

    List<NewzyCommentListGetResponseDto> getNewzyCommentList(Long newzyId);
}
