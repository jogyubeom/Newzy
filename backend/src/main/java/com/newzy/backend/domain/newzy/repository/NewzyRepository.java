package com.newzy.backend.domain.newzy.repository;

import com.newzy.backend.domain.newzy.entity.Newzy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NewzyRepository extends JpaRepository<Newzy, Long> {
    Newzy findByNewzyId(Long id);

    List<Newzy> findByTitle(String title);

    @Transactional
    default Newzy updateInfo(Newzy nzy) {
        Newzy newzy = findByNewzyId(nzy.getNewzyId());

        if (newzy == null) {
            new IllegalStateException("Newzy not found with ID: " + nzy.getNewzyId());
        }
        newzy.setTitle(nzy.getTitle());
        newzy.setContent(nzy.getContent());
        newzy.setCategory(nzy.getCategory());

        // Todo saveAndFlush() ?
        return save(newzy);
    }
//
//    @Transactional
    default void deleteById(Long newzyId) {
        Newzy newzy = findByNewzyId(newzyId);
        if (newzy == null) {
            new IllegalStateException("Newzy not found with ID: " + newzyId);
        }
        newzy.setIsDeleted(true);
        save(newzy);
    }



}
