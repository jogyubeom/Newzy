package com.newzy.backend.domain.user.controller;

import com.newzy.backend.domain.newzy.dto.request.NewzyRequestDTO;
import com.newzy.backend.domain.user.dto.request.UserInfoRequestDTO;
import com.newzy.backend.domain.user.service.UserServiceImpl;
import com.newzy.backend.global.exception.CustomIllegalStateException;
import com.newzy.backend.global.model.BaseResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserServiceImpl userService;
    private final UserServiceImpl userServiceImpl;

    @PostMapping
    @Operation(summary = "", description = "")
    public ResponseEntity<BaseResponseBody> create(@RequestBody @Validated UserInfoRequestDTO dto){
        log.info(">>> [POST] /user - 요청 파라미터: dto - {}", dto.toString());
         userServiceImpl.save(dto);

        return ResponseEntity.status(201).body(BaseResponseBody.of(201, "회원 등록이 완료되었습니다."));
    }

    @PatchMapping(value = "/{userId}")
    @Operation(summary = "", description = "")
    public ResponseEntity<BaseResponseBody> update(
            @PathVariable Long userId,
            @RequestBody @Validated UserInfoRequestDTO dto){
        log.info(">>> [PATCH] /user/{} - 요청 파라미터: userId - {},  dto - {}", userId, userId, dto.toString());
        if (userId == null) {
            throw new CustomIllegalStateException("해당 아이디의 유저를 찾을 수 없습니다." + userId);
        }
        userServiceImpl.updateUserInfo(userId, dto);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "회원 정보 수정이 완료되었습니다."));
    }

    @DeleteMapping(value = "/{userId}")
    @Operation(summary = "", description = "")
    public ResponseEntity<BaseResponseBody> delete(@PathVariable Long userId){
        log.info(">>> [DELETE] /user/{} - 요청 파라미터: userId - {}", userId, userId);
        userServiceImpl.deleteUserInfo(userId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "회원 삭제가 완료되었습니다."));
    }


}
