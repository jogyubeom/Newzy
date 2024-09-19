package com.newzy.backend.domain.newzy.controller;

import com.newzy.backend.domain.newzy.dto.request.NewzyRequestDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyResponseDTO;
import com.newzy.backend.domain.newzy.service.NewzyServiceImpl;
import com.newzy.backend.global.model.BaseResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/newzy")
public class NewzyController {

    private final NewzyServiceImpl newzyServiceImpl;

    @PostMapping("/new")
    @Operation(summary = "뉴지 게시글 추가", description = "새로운 뉴지를 등록합니다.")
    public ResponseEntity<BaseResponseBody> create(@RequestBody @Validated NewzyRequestDTO dto){

         newzyServiceImpl.save(dto);

        return ResponseEntity.status(201).body(BaseResponseBody.of(201, "뉴지 등록이 완료되었습니다."));
    }

    @GetMapping
    public ResponseEntity<List<NewzyResponseDTO>> getAllUsers(){
        List<NewzyResponseDTO> users = newzyServiceImpl.findNewzies();

        return ResponseEntity.status(200).body(users);
    }

    @PatchMapping(value = "/{newzyId}")
    public ResponseEntity<BaseResponseBody> updateNewzyInfo (
            @PathVariable("newzyId") Long newzyId,
            @RequestBody @Validated NewzyRequestDTO dto){

        newzyServiceImpl.update(newzyId, dto);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "뉴지 수정이 완료되었습니다."));
    }

    @DeleteMapping(value = "/{newzyId}")
    public ResponseEntity<BaseResponseBody> deleteNewzyInfo (@PathVariable("newzyId") Long newzyId){
        newzyServiceImpl.delete(newzyId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "뉴지 삭제가 완료되었습니다."));
    }



}














