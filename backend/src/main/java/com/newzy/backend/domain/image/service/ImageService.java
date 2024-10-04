package com.newzy.backend.domain.image.service;

import com.newzy.backend.domain.image.dto.NewzyImageGetResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    // TODO [이영원] 게시글 사진 추가 예정
    String[] uploadImages(MultipartFile[] images);

    String[] newzyUploadImages(MultipartFile[] images, Long newzyId, int thumbnailIdx);

    boolean deleteImage(String url);

    String loadImage(Long imageId);

    List<NewzyImageGetResponseDTO> loadImagesByNewzyId(Long newzyId);

    List<String> loadImageUrlsByNewzyId(Long newzyId);

    String loadThumbnailImageByNewzyId(Long newzyId);
}
