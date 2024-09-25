package com.newzy.backend.domain.newzy.controller;

import com.newzy.backend.domain.newzy.dto.request.NewzyRequestDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyResponseDTO;
import com.newzy.backend.domain.newzy.entity.Category;
import com.newzy.backend.domain.newzy.service.NewzyServiceImpl;
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
@RequestMapping("/newzy")
public class NewzyController {

    private final NewzyServiceImpl newzyServiceImpl;

    @PostMapping
    @Operation(summary = "뉴지 게시글 추가", description = "새로운 뉴지를 등록합니다.")
    public ResponseEntity<BaseResponseBody> create(@RequestBody @Validated NewzyRequestDTO dto){
        log.info(">>> [POST] /newzy create - 요청 파라미터: dto - {}", dto.toString());
         newzyServiceImpl.save(dto);

        return ResponseEntity.status(201).body(BaseResponseBody.of(201, "뉴지 등록이 완료되었습니다."));
    }

    @GetMapping
    @Operation(summary = "모든 뉴지 게시글 조회", description = "모든 뉴지 게시글을 조회합니다.")
    public ResponseEntity<Page<NewzyResponseDTO>> getNewzyList(
            @Parameter(description = "페이지 번호")
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "카테고리")
            @RequestParam(value = "category", required = false, defaultValue = "자유") Category category
    ){
        log.info(">>> [GET] /newzy getNewzyList - 요청 파라미터: page - {}, category - {}", page, category);
        Page<NewzyResponseDTO> newzyList = newzyServiceImpl.getNewzyList(page, category);

        return ResponseEntity.status(200).body(newzyList);
    }

    @PatchMapping(value = "/{newzyId}")
    @Operation(summary = "뉴지 게시글 수정", description = "해당 뉴지를 수정합니다.")
    public ResponseEntity<BaseResponseBody> updateNewzyInfo (
            @PathVariable("newzyId") Long newzyId,
            @RequestBody @Validated NewzyRequestDTO dto){
        log.info(">>> [PATCH] /newzy updateNewzyInfo - 요청 파라미터: newzyId - {}", newzyId);
        newzyServiceImpl.update(newzyId, dto);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "뉴지 수정이 완료되었습니다."));
    }

    @DeleteMapping(value = "/{newzyId}")
    @Operation(summary = "뉴지 게시글 삭제", description = "해당 뉴지를 삭제합니다.")
    public ResponseEntity<BaseResponseBody> deleteNewzyInfo (@PathVariable("newzyId") Long newzyId){
        log.info(">>> [DELETE] /newzy deleteNewzyInfo - 요청 파라미터: newzyId - {}", newzyId);

        newzyServiceImpl.delete(newzyId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "뉴지 삭제가 완료되었습니다."));
    }

//    @PatchMapping(value = "/{newzyId}/bookmark")
//    @Operation(summary = "뉴지 북마크", description = "해당 뉴지를 북마크합니다.")
//    public ResponseEntity<BaseResponseBody> bookmarkNewzy (
//            @PathVariable("newzyId") Long newzyId,
//            @RequestBody @Validated NewzyRequestDTO dto){
//
//        newzyServiceImpl.bookmark(newzyId, dto);
//
//        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "해당 뉴지를 북마크했습니다."));
//    }



}