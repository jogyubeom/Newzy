package com.newzy.backend.domain.newzy.controller;

import com.newzy.backend.domain.newzy.dto.request.NewzyCommentRequestDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyCommentListGetResponseDto;
import com.newzy.backend.domain.newzy.dto.response.NewzyCommentResponseDTO;
import com.newzy.backend.domain.newzy.service.NewzyCommentServiceImpl;
import com.newzy.backend.domain.user.service.UserService;
import com.newzy.backend.domain.user.service.UserServiceImpl;
import com.newzy.backend.global.exception.CustomIllegalStateException;
import com.newzy.backend.global.exception.EntityNotFoundException;
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
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/newzy/{newzyId}/comments")
public class NewzyCommentController {

     private final NewzyCommentServiceImpl newzyCommentServiceImpl;
     private final UserService userService;

     @GetMapping
     @Operation(summary = "뉴지 댓글 목록", description = "뉴지 댓글 목록을 불러옵니다.")
     public ResponseEntity<Map<String, Object>> getNewzyCommentList(
             @PathVariable("newzyId") Long newzyId,
             @Parameter(description = "페이지 번호")
             @RequestParam(value = "page", required = false, defaultValue = "0") int page
     ){
         log.info(">>> [GET] /newzy/{}/comments - 요청 파라미터: newzyId - {}, page - {}", newzyId, newzyId, page);
         Map<String, Object> commentList = newzyCommentServiceImpl.getNewzyCommentList(newzyId, page);

         return ResponseEntity.status(200).body(commentList);
     }


    @PostMapping
    @Operation(summary = "뉴지 댓글 추가", description = "새로운 뉴지 댓글을 등록합니다.")
    public ResponseEntity<BaseResponseBody> createComment(
            @PathVariable("newzyId") Long newzyId,
            @Parameter(description = "JWT", required = false)
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody @Validated NewzyCommentRequestDTO dto
            ){
        Long userId = 0L;
        if (token != null) {
            userId = userService.getUser(token).getUserId();
        }
        log.info(">> [POST] /newzy/{}/comments - 요청파라미터: newzyId - {}, dto - {}, userId - {}", newzyId, newzyId, dto.toString(), userId);

        if (dto == null) {
            throw new CustomIllegalStateException("해당 뉴지 댓글의 dto를 찾을 수 없습니다.: " + dto);
        }
        newzyCommentServiceImpl.saveComment(userId, newzyId, dto);

        return ResponseEntity.status(201).body(BaseResponseBody.of(201, "뉴지 댓글 등록이 완료되었습니다."));
    }


    @PatchMapping(value = "/{commentId}")
    @Operation(summary = "해당 뉴지 댓글 수정", description = "해당 뉴지의 댓글을 수정합니다.")
    public ResponseEntity<BaseResponseBody> updateNewzyComment(
            @PathVariable("newzyId")  Long newzyId,
            @PathVariable("commentId")  Long commentId,
            @Parameter(description = "JWT", required = false)
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody @Validated NewzyCommentRequestDTO dto
    ){
        Long userId = 0L;
        if (token != null) {
            userId = userService.getUser(token).getUserId();
        }
         log.info(">> [PATCH] /newzy/{}/comments/{} - 요청 파라미터: newzyId - {}, commentId - {}, userId - {}", newzyId, commentId, newzyId, commentId, userId);
         newzyCommentServiceImpl.updateComment(userId, commentId, dto);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "해당 뉴지의 댓글 수정이 완료되었습니다."));
    }


    @DeleteMapping(value = "/{newzyCommentId}")
    @Operation(summary = "해당 뉴지 댓글 삭제", description = "해당 뉴지의 댓글을 삭제합니다.")
    public ResponseEntity<BaseResponseBody> deleteNewzyComment(
            @PathVariable("newzyId")  Long newzyId,
            @PathVariable("newzyCommentId")  Long newzyCommentId,
            @Parameter(description = "JWT", required = false)
            @RequestHeader(value = "Authorization", required = false) String token
            ){
        Long userId = 0L;
        if (token != null) {
            userId = userService.getUser(token).getUserId();
        }
        log.info(">> [DELETE] /newzy/{}/comments/{} - 요청파라미터: newzyId - {}, commentId - {}, userId - {}", newzyId, newzyCommentId, newzyId, newzyCommentId, userId);
         newzyCommentServiceImpl.deleteComment(userId, newzyCommentId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "해당 뉴지의 댓글 삭제가 완료되었습니다."));
    }


}
