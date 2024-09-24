package com.newzy.backend.domain.newzy.dto.response;

import com.newzy.backend.domain.newzy.entity.Newzy;
import com.newzy.backend.domain.newzy.entity.NewzyComment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Schema(title = "NEWZY_COMMENT_REQ : 뉴지 댓글 응답 DTO")
public class NewzyCommentResponseDTO {

    @Schema(description = "뉴지 댓글 pk", example = "1")
    private Long newzyCommentId;

    @Schema(description = "뉴지 댓글", example = "comment")
    private String newzyComment;

    @Schema(description = "뉴지 댓글 수정여부", example = "false")
    private boolean isUpdated;

    @Schema(description = "뉴지 댓글 삭제여부", example = "false")
    private boolean isDeleted;

    @Schema(description = "해당 뉴지", example = "Newzy")
    private Long newzyId;

    @Schema(description = "부모 댓글")
    private NewzyComment parentComment;

//    @Schema(description = "유저", example = "User")
//    private User user;

    public NewzyCommentResponseDTO(Long newzyCommentId, String newzyComment, Long newzyId, boolean isUpdated, boolean isDeleted, NewzyComment parentComment) {
        this.newzyCommentId = newzyCommentId;
        this.newzyComment = newzyComment;
        this.newzyId = newzyId;
        this.isUpdated = isUpdated;
        this.isDeleted = isDeleted;
        this.parentComment = parentComment;
    }

    public static NewzyCommentResponseDTO convertToDTO(NewzyComment newzyComment) {
        if (newzyComment == null) { return null; }

        return new NewzyCommentResponseDTO(
                newzyComment.getNewzyCommentId(),
                newzyComment.getNewzyComment(),
                newzyComment.getNewzy().getNewzyId(),
                newzyComment.getIsUpdated(),
                newzyComment.getIsDeleted(),
                newzyComment.getParentComment()
        );
    }

}
