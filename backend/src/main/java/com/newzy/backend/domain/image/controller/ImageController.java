package com.newzy.backend.domain.image.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.newzy.backend.domain.image.exception.InvalidImageFileException;
import com.newzy.backend.domain.image.exception.S3FileUploadException;
import com.newzy.backend.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final AmazonS3 amazonS3;

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImages(@RequestPart(value = "profile") MultipartFile[] images) {
        log.info(">>> [POST] /api/images/upload - 이미지 업로드 요청");

        if (images == null || images.length == 0) {
            return ResponseEntity.badRequest().body("이미지 파일이 필요합니다.");
        }

        try {
            String[] imageUrls = imageService.uploadImages(images);
            return ResponseEntity.ok().body(imageUrls);  // 성공 시 이미지 URL 배열 반환
        } catch (InvalidImageFileException e) {
            log.error(">>> [POST] /api/images/upload - 빈 이미지 파일 업로드 시도됨: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (S3FileUploadException e) {
            log.error(">>> [POST] /api/images/upload - S3 업로드 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e) {
            log.error(">>> [POST] /api/images/upload - 예기치 못한 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 중 오류가 발생했습니다.");
        }

    }

}
