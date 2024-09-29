package com.newzy.backend.domain.newzy.service;

import com.newzy.backend.domain.newzy.dto.request.NewzyCommentRequestDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyCommentListGetResponseDto;
import com.newzy.backend.domain.newzy.dto.response.NewzyCommentResponseDTO;
import com.newzy.backend.domain.newzy.entity.Newzy;
import com.newzy.backend.domain.newzy.entity.NewzyComment;
import com.newzy.backend.domain.newzy.repository.NewzyCommentRepository;
import com.newzy.backend.domain.newzy.repository.NewzyCommentRepositorySupport;
import com.newzy.backend.domain.newzy.repository.NewzyRepository;
import com.newzy.backend.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
@ToString
public class NewzyCommentServiceImpl implements NewzyCommentService {

    private final NewzyCommentRepository newzyCommentRepository;
    private final NewzyCommentRepositorySupport newzyCommentRepositorySupport;
    private final NewzyRepository newzyRepository;

    @Override
    public List<NewzyCommentListGetResponseDto> getNewzyCommentList(Long newzyId, int page) {
        log.info(">>> newzyCommentServiceImpl getNewzyCommentList - newzyId: {}, page: {}", newzyId, page);
        int size = 10;
        List<NewzyCommentListGetResponseDto> commentList = newzyCommentRepositorySupport.findCommentList(page, size, newzyId);

        if (commentList.isEmpty()) {
            throw new EntityNotFoundException("일치하는 댓글 데이터를 조회할 수 없습니다.");
        }
        return commentList;
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