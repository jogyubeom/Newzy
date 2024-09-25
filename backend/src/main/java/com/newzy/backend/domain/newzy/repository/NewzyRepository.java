package com.newzy.backend.domain.newzy.repository;

import com.newzy.backend.domain.newzy.dto.response.NewzyResponseDTO;
import com.newzy.backend.domain.newzy.entity.Category;
import com.newzy.backend.domain.newzy.entity.Newzy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewzyRepository extends JpaRepository<Newzy, Long> {

    Page<Newzy> findByCategoryAndIsDeletedFalse(Category category, Pageable pageable);

    Newzy findByNewzyId(Long id);

    default Newzy updateNewzyInfo(Newzy nzy) {
        Newzy newzy = findByNewzyId(nzy.getNewzyId());

        if (newzy == null) {
            new IllegalStateException("Newzy not found with ID");
        }
        newzy.setTitle(nzy.getTitle());
        newzy.setContent(nzy.getContent());
        newzy.setCategory(nzy.getCategory());

        return save(newzy);
    }

    default void deleteNewzyById(Long newzyId) {
        Newzy newzy = findById(newzyId).orElseThrow(() -> new IllegalStateException("newzy not found with ID: " + newzyId));

        newzy.setIsDeleted(true);
        save(newzy);
    }

}
