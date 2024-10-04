package com.newzy.backend.domain.newzy.dto.response;

import com.newzy.backend.domain.image.entity.Image;
import com.newzy.backend.domain.newzy.entity.Newzy;
import com.newzy.backend.domain.newzy.entity.NewzyComment;
import com.newzy.backend.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Schema(title = "NEWZY_COMMENT_REQ : 뉴지 댓글 응답 DTO")
public class NewzyCommentResponseDTO {

    @Schema(description = "뉴지 댓글 pk", example = "1")
    private Long newzyCommentId;

    @Schema(description = "뉴지 댓글", example = "comment")
    private String newzyComment;

    @Schema(description = "뉴지 댓글 삭제여부", example = "false")
    private boolean isDeleted;

    @Schema(description = "뉴지 댓글 작성시간")
    private LocalDateTime createdAt;

    @Schema(description = "해당 뉴지", example = "Newzy")
    private Long newzyId;

    @Schema(description = "부모 댓글")
    private Long parentCommentId;

    @Schema(description = "유저", example = "User")
    private Long userId;

    @Schema(description = "이메일", example = "Email")
    private String email;

    @Schema(description = "닉네임", example = "Nickname")
    private String nickname;

    @Schema(description = "프로필 사진", example = "profile")
    private Image profile;

    public static NewzyCommentResponseDTO convertToDTO(NewzyComment newzyComment) {
        if (newzyComment == null) {
            throw new IllegalArgumentException("NewzyComment entity cannot be null");
        }
        return NewzyCommentResponseDTO.builder()
                .newzyCommentId(newzyComment.getNewzyCommentId())
                .newzyComment(newzyComment.getNewzyComment())
                .isDeleted(newzyComment.getIsDeleted())
                .createdAt(newzyComment.getCreatedAt())
                .newzyId(newzyComment.getNewzy().getNewzyId())
                .parentCommentId(newzyComment.getParentComment() != null ? newzyComment.getParentComment().getNewzyCommentId() : null)
                .userId(newzyComment.getUser().getUserId())
                .email(newzyComment.getUser().getEmail())
                .nickname(newzyComment.getUser().getNickname())
                .profile(newzyComment.getUser().getImage())
                .build();
    }

}
