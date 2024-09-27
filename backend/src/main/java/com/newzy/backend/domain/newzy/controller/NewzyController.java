package com.newzy.backend.domain.newzy.controller;

import com.newzy.backend.domain.newzy.dto.request.NewzyRequestDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyListGetResponseDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyResponseDTO;
import com.newzy.backend.domain.newzy.service.NewzyServiceImpl;
import com.newzy.backend.global.model.BaseResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/newzy")
public class NewzyController {

    private final NewzyServiceImpl newzyServiceImpl;

    @PostMapping
    @Operation(summary = "뉴지 게시글 추가", description = "새로운 뉴지를 등록합니다.")
    public ResponseEntity<BaseResponseBody> create(@RequestBody @Validated NewzyRequestDTO dto){
        log.info(">>> [POST] /newzyController create - 요청 파라미터: dto - {}", dto.toString());
        newzyServiceImpl.save(dto);

        return ResponseEntity.status(201).body(BaseResponseBody.of(201, "뉴지 등록이 완료되었습니다."));
    }

    @GetMapping
    @Operation(summary = "모든 뉴지 게시글 조회", description = "모든 뉴지 게시글을 조회합니다.")
    public ResponseEntity<List<NewzyListGetResponseDTO>> getNewzyList(
            @Parameter(description = "페이지 번호")
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "category (0: 시사, 1: 문화, 2: 자유)")
            @RequestParam(value = "category", required = false, defaultValue = "자유") String category
    ){
        log.info(">>> [GET] /newzyController getNewzyList - 요청 파라미터: page - {}, category - {}", page, category);

        int categoryInt;
        switch (category) {
            case "시사":
                categoryInt = 0;
                break;
            case "문화":
                categoryInt = 1;
                break;
            case "자유":
                categoryInt = 2;
                break;
            default:
                throw new IllegalArgumentException("Invalid category: " + category);
        }
        List<NewzyListGetResponseDTO> newzyList = newzyServiceImpl.getNewzyList(page, categoryInt);

        return ResponseEntity.status(200).body(newzyList);
    }

    @GetMapping(value = "/{newzyId}")
    @Operation(summary = "뉴지 상세 조회", description = "해당 뉴지를 상세 조회합니다.")
    public ResponseEntity<NewzyResponseDTO> getNewzy(@PathVariable Long newzyId){
        log.info(">>> [GET] /newzyController getNewzy - 요청 파라미터: newzyId - {}", newzyId);
        NewzyResponseDTO newzyDetail = newzyServiceImpl.getNewzyDetail(newzyId);

        return ResponseEntity.status(200).body(newzyDetail);
    }

    @PatchMapping(value = "/{newzyId}")
    @Operation(summary = "뉴지 게시글 수정", description = "해당 뉴지를 수정합니다.")
    public ResponseEntity<BaseResponseBody> updateNewzyInfo (
            @PathVariable("newzyId") Long newzyId,
            @RequestBody @Validated NewzyRequestDTO dto){
        log.info(">>> [PATCH] /newzyController updateNewzyInfo - 요청 파라미터: newzyId - {}", newzyId);
        newzyServiceImpl.update(newzyId, dto);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "뉴지 수정이 완료되었습니다."));
    }

    @DeleteMapping(value = "/{newzyId}")
    @Operation(summary = "뉴지 게시글 삭제", description = "해당 뉴지를 삭제합니다.")
    public ResponseEntity<BaseResponseBody> deleteNewzyInfo (@PathVariable("newzyId") Long newzyId){
        log.info(">>> [DELETE] /newzyController deleteNewzyInfo - 요청 파라미터: newzyId - {}", newzyId);
        newzyServiceImpl.delete(newzyId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "뉴지 삭제가 완료되었습니다."));
    }

    @PostMapping(value = "/newzy-bookmark/{newzyId}")
    @Operation(summary = "뉴지 북마크 등록", description = "해당 뉴지를 북마크합니다.")
    public ResponseEntity<BaseResponseBody> bookmarkNewzy (
            @PathVariable("newzyId") Long newzyId){
        log.info(">>> [POST] /newzyController bookmarkNewzy - 요청 파라미터: newzyId - {}", newzyId);
        newzyServiceImpl.bookmark(newzyId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "해당 뉴지를 북마크했습니다."));
    }

    @DeleteMapping(value = "/newzy-bookmark/{newzyId}/{bookmarkId}")
    @Operation(summary = "뉴지 북마크 삭제", description = "해당 뉴지 북마크를 삭제합니다.")
    public ResponseEntity<BaseResponseBody> deleteBookmark (
            @PathVariable("newzyId") Long newzyId,
            @PathVariable("bookmarkId") Long bookmarkId
    ){
        log.info(">>> [DELETE] /newzyController deleteBookmark - 요청 파라미터: newzyId - {}, bookmarkId - {}", newzyId, bookmarkId);
        newzyServiceImpl.deleteBookmark(bookmarkId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "해당 뉴지를 북마크를 삭제했습니다."));
    }

    @PostMapping("/newzy-like/{newzyId}")
    @Operation(summary = "뉴지 좋아요 등록", description = "해당 뉴지가 좋습니다.")
    public ResponseEntity<BaseResponseBody> likeNewzy (@PathVariable("newzyId") Long newzyId
    ){
        log.info(">>> [POST] /newzyController likeNewzy - 요청 파라미터: newzyId - {}", newzyId);
        newzyServiceImpl.likeNewzy(newzyId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "해당 뉴지가 좋습니다."));
    }

    @DeleteMapping(value = "newzy-like/{newzyId}/{newzyLikeId}")
    @Operation(summary = "뉴지 좋아요 취소", description = "해당 뉴지 좋아요를 취소합니다.")
    public ResponseEntity<BaseResponseBody> deleteNewzyLike (
            @PathVariable("newzyId") Long newzyId,
            @PathVariable("newzyLikeId") Long newzyLikeId
    ){
        log.info(">>> [DELETE] /newzyController deleteBookmark - 요청 파라미터: newzyId - {}, newzyLikeId - {}", newzyId, newzyLikeId);
        newzyServiceImpl.deleteLike(newzyLikeId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "해당 뉴지의 좋아요를 삭제했습니다."));
    }


}