package com.newzy.backend.domain.newzy.service;

import com.newzy.backend.domain.newzy.dto.request.NewzyCommentRequestDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyCommentListGetResponseDto;
import com.newzy.backend.domain.newzy.dto.response.NewzyCommentResponseDTO;
import com.newzy.backend.domain.newzy.entity.Newzy;
import com.newzy.backend.domain.newzy.entity.NewzyComment;
import com.newzy.backend.domain.newzy.repository.NewzyCommentRepository;
import com.newzy.backend.domain.newzy.repository.NewzyCommentRepositorySupport;
import com.newzy.backend.domain.newzy.repository.NewzyRepository;
import com.newzy.backend.global.exception.CustomIllegalStateException;
import com.newzy.backend.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
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
        Newzy newzy = newzyRepository.findById(newzyId).orElseThrow(() -> new EntityNotFoundException("해당하는 뉴지 엔티티를 찾을 수 없습니다.: " + newzyId));

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
        // TODO : 삭제한 코멘트 중복 삭제되지 않게 예외처리!
        // TODO : 유저 토큰 처리되면, 해당 유저 아이디로 댓글을 조회한뒤 확인되면 삭제하는 로직 추가 필요
        NewzyComment comment = newzyCommentRepository.findById(newzyCommentId).orElseThrow(() -> new EntityNotFoundException("해당하는  댓글 엔티티를 찾을 수 없습니다.: " + newzyCommentId));
        
        newzyCommentRepository.deleteNewzyCommentById(newzyCommentId);
    }


}