package com.newzy.backend.domain.newzy.repository;

import com.newzy.backend.domain.newzy.entity.NewzyComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewzyCommentRepository extends JpaRepository<NewzyComment, Long> {

    NewzyComment findByNewzy_NewzyIdAndNewzyCommentIdAndIsDeletedFalse(Long newzyId, Long newzyCommentId);

    Page<NewzyComment> findAllByNewzy_NewzyIdAndIsDeletedFalse(Long newzyId, Pageable pageable);

    default NewzyComment updateNewzyCommentById(NewzyComment newzyComment) {
        NewzyComment updatedComment = findById(newzyComment.getNewzyCommentId()).orElse(null);

        if (updatedComment == null) {
            new IllegalArgumentException("Newzy comment not found with ID");
        }

        updatedComment.setNewzyComment(newzyComment.getNewzyComment());

        return save(updatedComment);
    }

    default void deleteNewzyCommentById(Long newzyCommentId) {
        NewzyComment newzyComment = findById(newzyCommentId).orElse(null);
        if (newzyComment == null) {
            new IllegalArgumentException("Newzy comment not found with ID");
        }
        newzyComment.setIsDeleted(true);
        save(newzyComment);
    }
}
