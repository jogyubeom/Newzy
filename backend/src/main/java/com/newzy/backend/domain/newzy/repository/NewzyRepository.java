package com.newzy.backend.domain.newzy.repository;

import com.newzy.backend.domain.newzy.entity.Newzy;
import com.newzy.backend.global.exception.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewzyRepository extends JpaRepository<Newzy, Long> {


    Newzy findByNewzyId(Long id);

    default Newzy updateNewzyInfo(Newzy nzy) {
        Newzy newzy = findByNewzyId(nzy.getNewzyId());

        if (newzy == null) {
            new IllegalStateException("Newzy not found with ID");
        }
        newzy.setUser(nzy.getUser());
        newzy.setNewzyId(nzy.getNewzyId());
        newzy.setTitle(nzy.getTitle());
        newzy.setContent(nzy.getContent());
        newzy.setContentText(nzy.getContentText());
        newzy.setCategory(nzy.getCategory());
        newzy.setThumbnail(nzy.getThumbnail());

        return save(newzy);
    }

    default void deleteNewzyById(Long newzyId) {
        Newzy newzy = findById(newzyId).orElseThrow(() -> new EntityNotFoundException("해당하는 뉴지 데이터를 찾을 수 없습니다. newzyID: " + newzyId));

        newzy.setIsDeleted(true);
        save(newzy);
    }

    // 특정 userId에 해당하는 유저가 만든 뉴지 ( isDeleted = false )인 개수 조회
    Long countByUserUserIdAndIsDeletedFalse(Long userId);
}
