package com.newzy.backend.domain.newzy.dto.request;

import com.newzy.backend.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
    private String newzyComment;

    @Schema(description = "해당 뉴지 댓글 Id", example = "NewzyCommentId")
    private Long newzyCommentId;

    @Schema(description = "해당 뉴지 부모 댓글 Id", example = "NewzyParentCommentId")
    private Long newzyParentCommentId;

    @Schema(description = "뉴지 게시글 Id")
    private Long newzyId;

    // TODO : 유저 토큰 추가 후 추수 다시 수정
//    @Schema(description = "유저", example = "User")
//    private User user;

    public NewzyCommentRequestDTO(String newzyComment) {
        this.newzyComment = newzyComment;
    }


}