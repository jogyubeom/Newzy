package com.newzy.backend.domain.image.service;

import com.newzy.backend.domain.image.dto.NewzyImageGetResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    String[] uploadImages(MultipartFile[] images);

    String[] newzyUploadImages(MultipartFile[] images, Long newzyId, int thumbnailIdx);

    boolean deleteImage(String url);

    String loadImage(Long imageId);

    List<NewzyImageGetResponseDTO> loadImagesByNewzyId(Long newzyId);

    List<String> loadImageUrlsByNewzyId(Long newzyId);

    String loadThumbnailImageByNewzyId(Long newzyId);
}
