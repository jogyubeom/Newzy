package com.newzy.backend.domain.newzy.dto.response;

import com.newzy.backend.domain.newzy.entity.NewzyComment;
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
    private String createdAt;
    private Long newzyId;
    private Long parentCommentId;

    public static NewzyCommentListGetResponseDto convertToDTO(NewzyComment newzyComment) {
        if (newzyComment == null) {
            throw new IllegalArgumentException("Cannot convert null to NewzyCommentListGetResponseDto");
        };

        return NewzyCommentListGetResponseDto.builder()
                .newzyCommentId(newzyComment.getNewzyCommentId())
                .newzyComment(newzyComment.getNewzyComment())
                .createdAt(newzyComment.getCreatedAt().toString())
                .newzyId(newzyComment.getNewzy().getNewzyId())
                .parentCommentId(newzyComment.getParentComment() != null ? newzyComment.getParentComment().getNewzyCommentId() : null)
                .build();
    }
}

