package com.newzy.backend.domain.newzy.controller;

import com.newzy.backend.domain.newzy.dto.request.NewzyCommentRequestDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyCommentListGetResponseDto;
import com.newzy.backend.domain.newzy.dto.response.NewzyCommentResponseDTO;
import com.newzy.backend.domain.newzy.service.NewzyCommentServiceImpl;
import com.newzy.backend.global.exception.CustomIllegalStateException;
import com.newzy.backend.global.model.BaseResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/newzy/{newzyId}/comments")
public class NewzyCommentController {

     private final NewzyCommentServiceImpl newzyCommentServiceImpl;

     @GetMapping
     @Operation(summary = "뉴지 댓글 목록", description = "뉴지 댓글 목록을 불러옵니다.")
     public ResponseEntity<List<NewzyCommentListGetResponseDto>> getNewzyCommentList(
             @PathVariable("newzyId") Long newzyId,
             @Parameter(description = "페이지 번호")
             @RequestParam(value = "page", required = false, defaultValue = "0") int page
     ){
         log.info(">>> [GET] /newzy/{}/comments - 요청 파라미터: newzyId - {}, page - {}", newzyId, newzyId, page);
         List<NewzyCommentListGetResponseDto> commentList = newzyCommentServiceImpl.getNewzyCommentList(newzyId, page);

         return ResponseEntity.status(200).body(commentList);
     }

    @PostMapping
    @Operation(summary = "뉴지 댓글 추가", description = "새로운 뉴지 댓글을 등록합니다.")
    public ResponseEntity<BaseResponseBody> createComment(
            @PathVariable("newzyId") Long newzyId,
            @RequestBody @Validated NewzyCommentRequestDTO dto
            ){
        log.info(">> [POST] /newzy/{}/comments - 요청파라미터: newzyId - {}, dto - {}", newzyId, newzyId, dto.toString());
        if (dto == null) {
            throw new CustomIllegalStateException("해당 뉴지 댓글의 dto를 찾을 수 없습니다.: " + dto);
        }
        newzyCommentServiceImpl.saveComment(newzyId, dto);

        return ResponseEntity.status(201).body(BaseResponseBody.of(201, "뉴지 댓글 등록이 완료되었습니다."));
    }

    @PatchMapping(value = "/{commentId}")
    @Operation(summary = "해당 뉴지 댓글 수정", description = "해당 뉴지의 댓글을 수정합니다.")
    public ResponseEntity<BaseResponseBody> updateNewzyComment(
            @PathVariable("newzyId")  Long newzyId,
            @PathVariable("commentId")  Long commentId,
            @RequestBody @Validated NewzyCommentRequestDTO dto
    ){
         log.info(">> [PATCH] /newzy/{}/comments/{} - 요청파라미터: newzyId - {}, commentId - {}, dto - {}", newzyId, commentId, newzyId, commentId, dto.toString());
         newzyCommentServiceImpl.updateComment(commentId, dto);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "해당 뉴지의 댓글 수정이 완료되었습니다."));
    }

    @DeleteMapping(value = "/{newzyCommentId}")
    @Operation(summary = "해당 뉴지 댓글 삭제", description = "해당 뉴지의 댓글을 삭제합니다.")
    public ResponseEntity<BaseResponseBody> deleteNewzyComment(
            @PathVariable("newzyId")  Long newzyId,
            @PathVariable("newzyCommentId")  Long newzyCommentId
    ){
        log.info(">> [DELETE] /newzy/{}/comments/{} - 요청파라미터: newzyId - {}, commentId - {}", newzyId, newzyCommentId, newzyId, newzyCommentId);
         newzyCommentServiceImpl.deleteComment(newzyCommentId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "해당 뉴지의 댓글 삭제가 완료되었습니다."));
    }


}
