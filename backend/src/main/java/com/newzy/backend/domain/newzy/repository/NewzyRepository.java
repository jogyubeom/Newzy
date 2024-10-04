package com.newzy.backend.domain.newzy.repository;

import com.newzy.backend.domain.newzy.entity.Newzy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewzyRepository extends JpaRepository<Newzy, Long> {


    Newzy findByNewzyId(Long id);
    List<Newzy> findTop3ByIsDeletedFalseOrderByHitDesc();

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
