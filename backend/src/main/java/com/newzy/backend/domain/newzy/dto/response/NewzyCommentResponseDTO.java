package com.newzy.backend.domain.newzy.dto.response;

import com.newzy.backend.domain.newzy.entity.Newzy;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
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
    private Newzy newzy;

//    @Schema(description = "유저", example = "User")
//    private User user;

    @Schema(description = "해당 뉴지 자식 댓글", example = "Children comments")
    private List<NewzyCommentResponseDTO> childrenComments;

    public NewzyCommentResponseDTO(Long newzyCommentId, String newzyComment) {
        this.newzyCommentId = newzyCommentId;
        this.newzyComment = newzyComment;
    }


}
