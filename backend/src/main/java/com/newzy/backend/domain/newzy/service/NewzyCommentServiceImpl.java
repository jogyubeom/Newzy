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
    private final NewzyRepository newzyRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<NewzyCommentResponseDTO> getNewzyCommentListByNewzyId(Long newzyId, int page, int size) {
        log.info(">>> newzyCommentServiceImpl getNewzyCommentList - newzyId: {}, page: {}, size: {}", newzyId, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<NewzyComment> newzyCommentPage = newzyCommentRepository.findAllByNewzy_NewzyIdAndIsDeletedFalse(newzyId, pageable);

        // 새로운 List를 생성하여 isDeleted가 false인 항목들만 필터링
        List<NewzyCommentResponseDTO> filteredComments = new ArrayList<>();

        for (NewzyComment newzyComment : newzyCommentPage.getContent()) {  // getContent()로 리스트 변환
            if (!newzyComment.getIsDeleted()) {  // isDeleted 필터링
                NewzyCommentResponseDTO responseDTO = NewzyCommentResponseDTO.convertToDTO(newzyComment);
                filteredComments.add(responseDTO);
            }
        }

        // 필터링한 리스트를 Page로 다시 변환
        return new PageImpl<>(filteredComments, pageable, newzyCommentPage.getTotalElements());
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