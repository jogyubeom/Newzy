package com.newzy.backend.domain.newzy.service;

import com.newzy.backend.domain.newzy.dto.request.NewzyCommentRequestDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyCommentListGetResponseDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyCommentResponseDTO;
import com.newzy.backend.domain.newzy.entity.Newzy;
import com.newzy.backend.domain.newzy.entity.NewzyComment;
import com.newzy.backend.domain.newzy.repository.NewzyCommentRepository;
import com.newzy.backend.domain.newzy.repository.NewzyCommentRepositorySupport;
import com.newzy.backend.domain.newzy.repository.NewzyRepository;
import com.newzy.backend.domain.user.entity.User;
import com.newzy.backend.domain.user.repository.UserRepository;
import com.newzy.backend.global.exception.CustomIllegalStateException;
import com.newzy.backend.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final UserRepository userRepository;

    @Override
    public List<NewzyCommentListGetResponseDTO> getNewzyCommentList(Long newzyId) {

        int size = 10;
        List<NewzyCommentListGetResponseDTO> commentList = newzyCommentRepositorySupport.findCommentList(newzyId);

//        if (commentList.isEmpty()) {
//            throw new EntityNotFoundException("일치하는 댓글 데이터를 조회할 수 없습니다.");
//        }
        return commentList;
    }


    @Override
    public void saveComment(Long userId, Long newzyId, NewzyCommentRequestDTO dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당하는 유저 엔티티를 찾을 수 없습니다. : " + userId));
        Newzy newzy = newzyRepository.findById(newzyId).orElseThrow(() -> new EntityNotFoundException("해당하는 뉴지 엔티티를 찾을 수 없습니다.: " + newzyId));

        // 경험치 업데이트
        user.setExp(user.getExp() + 10);

        NewzyComment newzyComment = NewzyComment.convertToEntityByNewzyId(dto, user, newzy);

        newzyCommentRepository.save(newzyComment);
    }


    @Override
    public NewzyCommentResponseDTO updateComment(Long userId, Long newzyCommentId, NewzyCommentRequestDTO dto) {

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당하는 유저 엔티티를 찾을 수 없습니다. : " + userId));

        NewzyComment comment = newzyCommentRepository.findById(newzyCommentId).orElseThrow(() -> new EntityNotFoundException("해당하는 뉴지 댓글 데이터를 찾을 수 없습니다. : " + newzyCommentId));

        NewzyComment updatedNewzyComment = NewzyComment.convertToEntityByNewzyCommentId(user, newzyCommentId, dto);
        NewzyComment newzyComment = newzyCommentRepository.updateNewzyCommentById(updatedNewzyComment);
        NewzyCommentResponseDTO commentResponseDTO = NewzyCommentResponseDTO.convertToDTO(newzyComment);

        return commentResponseDTO;
    }


    @Override
    public void deleteComment(Long userId, Long newzyCommentId) {

        NewzyComment comment = newzyCommentRepository.findById(newzyCommentId).orElseThrow(() -> new EntityNotFoundException("해당하는  댓글 엔티티를 찾을 수 없습니다.: " + newzyCommentId));

        if (comment.getIsDeleted()) {
            throw new CustomIllegalStateException("이미 삭제된 뉴지 댓글 입니다.");
        }

        if (userId.equals(comment.getUser().getUserId())) {
            newzyCommentRepository.deleteById(newzyCommentId);
        }
    }

}