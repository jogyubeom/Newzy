package com.newzy.backend.domain.newzy.controller;

import com.newzy.backend.domain.newzy.dto.request.NewzyListGetRequestDTO;
import com.newzy.backend.domain.newzy.dto.request.NewzyRequestDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyImageResponseDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyListGetResponseDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyResponseDTO;
import com.newzy.backend.domain.newzy.service.NewzyService;
import com.newzy.backend.domain.user.service.UserService;
import com.newzy.backend.global.exception.CustomIllegalStateException;
import com.newzy.backend.global.exception.NoTokenRequestException;
import com.newzy.backend.global.model.BaseResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/newzy")
public class NewzyController {

    private final NewzyService newzyService;
    private final UserService userService;


    @PostMapping
    @Operation(summary = "뉴지 게시글 추가", description = "새로운 뉴지를 등록합니다.")
    public ResponseEntity<BaseResponseBody> create(
            @Parameter(description = "JWT", required = false)
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody @Validated NewzyRequestDTO dto,
            @RequestPart(value = "images", required = false) MultipartFile[] images
    ) {
        Long userId = 0L;
        if (token != null) {
            userId = userService.getUser(token).getUserId();
        } else {
            throw new NoTokenRequestException("유효한 유저토큰이 없습니다.");
        }

        log.info(">>> [POST] /newzy - 요청 파라미터: dto - {}, userId - {}", dto.toString(), userId);
        dto.setImages(images);
        newzyService.save(userId, dto);

        return ResponseEntity.status(201).body(BaseResponseBody.of(201, "뉴지 등록이 완료되었습니다."));
    }


    @GetMapping
    @Operation(summary = "모든 뉴지 게시글 조회", description = "모든 뉴지 게시글을 조회합니다.")
    public ResponseEntity<Map<String, Object>> getNewzyList(
            @Parameter(description = "페이지 번호")
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @Parameter(description = "category (0: 시사, 1: 문화, 2: 자유)")
            @RequestParam(value = "category", required = false, defaultValue = "3") int category,
            @Parameter(description = "정렬기준")
            @RequestParam(value = "sort", required = false, defaultValue = "0") int sort,
            @Parameter(description = "키워드")
            @RequestParam(value = "keyword", required = false) String keyword,
            @Parameter(description = "JWT", required = false)
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        Long userId = 0L;
        if (token != null) {
            userId = userService.getUser(token).getUserId();
        } else {

        }
        log.info(">>> [GET] /newzy - 요청 파라미터: page - {}, category - {}, keyword - {}, sort - {}, userId - {}", page, category, keyword, sort, userId);

        NewzyListGetRequestDTO requestDTO = new NewzyListGetRequestDTO(page, category, sort, keyword);

        Map<String, Object> newzyList = newzyService.getNewzyList(requestDTO, userId);

        return ResponseEntity.status(200).body(newzyList);
    }

    @GetMapping(value = "/{newzyId}")
    @Operation(summary = "뉴지 상세 조회", description = "해당 뉴지를 상세 조회합니다.")
    public ResponseEntity<NewzyResponseDTO> getNewzy(
            @Parameter(description = "JWT", required = false)
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long newzyId
    ) {
        Long userId = 0L;
        if (token != null) {
            userId = userService.getUser(token).getUserId();
        }

        log.info(">>> [GET] /newzy/{} - 요청 파라미터: userId - {}, newzyId - {}", newzyId, userId, newzyId);
        NewzyResponseDTO newzyDetail = newzyService.getNewzyDetail(userId, newzyId);

        return ResponseEntity.status(200).body(newzyDetail);
    }


    @GetMapping(value = "/hot")
    @Operation(summary = "많이 본 뉴지 조회", description = "조회수가 많은 뉴지를 조회합니다.")
    public ResponseEntity<List<NewzyListGetResponseDTO>> getHotNewzy() {
        log.info(">>> [GET] /newzy/hot - 요청 파라미터");
        List<NewzyListGetResponseDTO> hotNewzyList = newzyService.getHotNewzyList();

        return ResponseEntity.status(200).body(hotNewzyList);
    }


    @PatchMapping(value = "/{newzyId}")
    @Operation(summary = "뉴지 게시글 수정", description = "해당 뉴지를 수정합니다.")
    public ResponseEntity<BaseResponseBody> updateNewzyInfo(
            @PathVariable("newzyId") Long newzyId,
            @Parameter(description = "JWT", required = false)
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody @Validated NewzyRequestDTO dto) {
        Long userId = 0L;
        if (token != null) {
            userId = userService.getUser(token).getUserId();
        } else {
            throw new NoTokenRequestException("유효한 유저 토큰이 없습니다.");
        }

        log.info(">>> [PATCH] /newzy/{} - 요청 파라미터: newzyId - {}, userId - {}", newzyId, newzyId, userId);
        newzyService.update(userId, newzyId, dto);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "뉴지 수정이 완료되었습니다."));
    }


    @DeleteMapping(value = "/{newzyId}")
    @Operation(summary = "뉴지 게시글 삭제", description = "해당 뉴지를 삭제합니다.")
    public ResponseEntity<BaseResponseBody> deleteNewzyInfo(
            @PathVariable("newzyId") Long newzyId,
            @Parameter(description = "JWT", required = false)
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        Long userId = 0L;
        if (token != null) {
            userId = userService.getUser(token).getUserId();
        } else {
            throw new NoTokenRequestException("유효한 유저 토큰이 없습니다.");
        }

        log.info(">>> [DELETE] /newzy/{} - 요청 파라미터: newzyId - {}, userId - {}", newzyId, newzyId, userId);
        newzyService.delete(userId, newzyId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "뉴지 삭제가 완료되었습니다."));
    }


    @PostMapping(value = "/{newzyId}/bookmark")
    @Operation(summary = "뉴지 북마크 등록", description = "해당 뉴지를 북마크합니다.")
    public ResponseEntity<BaseResponseBody> bookmarkNewzy(
            @PathVariable("newzyId") Long newzyId,
            @Parameter(description = "JWT", required = true)
            @RequestHeader(value = "Authorization", required = true) String token
    ) {
        if (newzyId == null) {
            throw new CustomIllegalStateException("해당 아이디의 뉴지를 찾을 수 없습니다.: " + newzyId);
        }

        Long userId = 0L;
        if (token != null) {
            userId = userService.getUser(token).getUserId();
        } else {
            throw new NoTokenRequestException("유효한 유저 토큰이 없습니다.");
        }

        log.info(">>> [POST] /newzy/{}/bookmark - 요청 파라미터: newzyId - {}, userId - {}", newzyId, newzyId, userId);
        newzyService.bookmark(userId, newzyId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "해당 뉴지를 북마크했습니다."));
    }


    @DeleteMapping(value = "/{newzyId}/bookmark")
    @Operation(summary = "뉴지 북마크 삭제", description = "해당 뉴지 북마크를 삭제합니다.")
    public ResponseEntity<BaseResponseBody> deleteBookmark(
            @PathVariable("newzyId") Long newzyId,
            @Parameter(description = "JWT", required = true)
            @RequestHeader(value = "Authorization", required = true) String token
    ) {
        Long userId = 0L;
        if (token != null) {
            userId = userService.getUser(token).getUserId();
        } else {
            throw new NoTokenRequestException("유효한 유저 토큰이 없습니다.");
        }
        log.info(">>> [DELETE] /newzy/{}/bookmark - 요청 파라미터: newzyId - {}, userId - {}", newzyId, newzyId, userId);
        newzyService.deleteBookmark(userId, newzyId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "해당 뉴지를 북마크를 삭제했습니다."));
    }


    @PostMapping("/{newzyId}/like")
    @Operation(summary = "뉴지 좋아요 등록", description = "해당 뉴지가 좋습니다.")
    public ResponseEntity<BaseResponseBody> likeNewzy(
            @PathVariable("newzyId") Long newzyId,
            @Parameter(description = "JWT", required = true)
            @RequestHeader(value = "Authorization", required = true) String token
    ) {
        if (newzyId == null) {
            throw new CustomIllegalStateException("해당 아이디의 뉴지를 찾을 수 없습니다.: " + newzyId);
        }

        Long userId = 0L;
        if (token != null) {
            userId = userService.getUser(token).getUserId();
        } else {
            throw new NoTokenRequestException("유효한 유저 토큰이 없습니다.");
        }

        log.info(">>> [POST] /newzy/{}/like - 요청 파라미터: newzyId - {}, userId - {}", newzyId, newzyId, userId);

        newzyService.likeNewzy(userId, newzyId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "해당 뉴지가 좋습니다."));
    }


    @DeleteMapping(value = "/{newzyId}/like")
    @Operation(summary = "뉴지 좋아요 취소", description = "해당 뉴지 좋아요를 취소합니다.")
    public ResponseEntity<BaseResponseBody> deleteNewzyLike(
            @PathVariable("newzyId") Long newzyId,
            @Parameter(description = "JWT", required = true)
            @RequestHeader(value = "Authorization", required = true) String token
    ) {
        Long userId = 0L;
        if (token != null) {
            userId = userService.getUser(token).getUserId();
        } else {
            throw new NoTokenRequestException("유효한 유저 토큰이 없습니다.");
        }

        log.info(">>> [DELETE] /newzy/{}/like - 요청 파라미터: newzyId - {}, userId - {}", newzyId, newzyId, userId);
        newzyService.deleteNewzyLike(userId, newzyId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "해당 뉴지의 좋아요를 삭제했습니다."));
    }

    @GetMapping(value = "/upload-image")
    @Operation(summary = "뉴지 이미지 url로 변환", description = "이미지 파일을 뉴지에 들어갈 url로 바꿔줍니다.")
    public ResponseEntity<NewzyImageResponseDTO> convertImgUrl(
            @Parameter(description = "JWT", required = true)
            @RequestHeader(value = "Authorization", required = true) String token,
            @RequestPart(value = "image", required = false) MultipartFile[] image
    ) {

        NewzyImageResponseDTO newzyImageResponseDTO = newzyService.convertImgUrl(image);

        return ResponseEntity.status(200).body(newzyImageResponseDTO);
    }


}