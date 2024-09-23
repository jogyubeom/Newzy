package com.newzy.backend.domain.newzy.repository;

import com.newzy.backend.domain.newzy.entity.Newzy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NewzyRepository extends JpaRepository<Newzy, Long> {

    Newzy findByNewzyId(Long id);

    @Query("SELECT n FROM Newzy n WHERE n.isDeleted = false")
    List<Newzy> findAllActiveNewzies();

    @Transactional
    default Newzy updateNewzyInfo(Newzy nzy) {
        Newzy newzy = findByNewzyId(nzy.getNewzyId());

        if (newzy == null) {
            new IllegalStateException("Newzy not found with ID");
        }
        newzy.setTitle(nzy.getTitle());
        newzy.setContent(nzy.getContent());
        newzy.setCategory(nzy.getCategory());

        // Todo saveAndFlush() ?
        return save(newzy);
    }


    @Transactional
    default void deleteNewzyById(Long newzyId) {
        Newzy newzy = findById(newzyId).orElseThrow(() -> new IllegalStateException("newzy not found with ID: " + newzyId));

        newzy.setIsDeleted(true);
        System.out.println(newzy.toString());
        save(newzy);
    }



}
