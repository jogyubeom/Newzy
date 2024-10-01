package com.newzy.backend.domain.newzy.dto.response;

import com.newzy.backend.domain.newzy.entity.NewzyComment;
import com.newzy.backend.global.exception.EntityNotFoundException;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
@ToString
public class NewzyCommentListGetResponseDto {

    private Long newzyCommentId;
    private String newzyComment;
    private LocalDateTime createdAt;
    private Long newzyId;
    private Long parentCommentId;

    public static NewzyCommentListGetResponseDto convertToDTO(NewzyComment newzyComment) {
        if (newzyComment == null) {
            throw new EntityNotFoundException("해당되는 뉴지 댓글 객체가 없습니다.");
        };

        return NewzyCommentListGetResponseDto.builder()
                .newzyCommentId(newzyComment.getNewzyCommentId())
                .newzyComment(newzyComment.getNewzyComment())
                .createdAt(newzyComment.getCreatedAt())
                .newzyId(newzyComment.getNewzy().getNewzyId())
                .parentCommentId(newzyComment.getParentComment() != null ? newzyComment.getParentComment().getNewzyCommentId() : null)
                .build();
    }
}

