package com.newzy.backend.domain.image.repository;

import com.newzy.backend.domain.image.entity.NewzyImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewzyImageRepository extends JpaRepository<NewzyImage, Long> {
    List<NewzyImage> findByNewzyNewzyIdAndImageIsDeletedFalseOrderByOrderAsc(Long newzyId);

    Optional<NewzyImage> findByNewzyNewzyIdAndIsThumbnailTrue(Long newzyId);
}
