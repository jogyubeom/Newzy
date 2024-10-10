package com.newzy.backend.domain.newzy.dto.request;

import com.newzy.backend.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Schema(title = "NEWZY_COMMENT_REQ : 뉴지 댓글 요청 DTO")
public class NewzyCommentRequestDTO {

    @NotNull(message = "댓글 내용을 입력해주세요.")
    @Schema(description = "뉴지 댓글", example = "comment")
//    @Size(max = 500, message = "본문은 최대 500자까지 입력할 수 있습니다.")
    private String newzyComment;

    @Schema(description = "해당 뉴지 댓글 Id", example = "NewzyCommentId")
    private Long newzyCommentId;

    @Schema(description = "해당 뉴지 부모 댓글 Id", example = "NewzyParentCommentId")
    private Long newzyParentCommentId;

    @Schema(description = "뉴지 게시글 Id")
    private Long newzyId;

    @Schema(description = "유저", example = "User")
    private Long userId;

    public NewzyCommentRequestDTO(String newzyComment, User user) {
        this.newzyComment = newzyComment;
        this.userId = user.getUserId();

    }


}
