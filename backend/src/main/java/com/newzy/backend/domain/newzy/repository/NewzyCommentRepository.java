package com.newzy.backend.domain.newzy.repository;

import com.newzy.backend.domain.newzy.entity.Newzy;
import com.newzy.backend.domain.newzy.entity.NewzyComment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewzyCommentRepository extends JpaRepository<NewzyComment, Long> {

    @Transactional
    default NewzyComment updateNewzyCommentById(NewzyComment newzyComment) {
       NewzyComment updatedComment = findById(newzyComment.getNewzyCommentId()).orElse(null);

       if (updatedComment == null) {
           new IllegalArgumentException("Newzy comment not found with ID");
       }

       updatedComment.setNewzyComment(newzyComment.getNewzyComment());

       return save(updatedComment);
   }

   @Transactional
   default void deleteNewzyCommentById(Long newzyCommentId) {
       NewzyComment newzyComment = findById(newzyCommentId).orElse(null);
        if (newzyComment == null) {
            new IllegalArgumentException("Newzy comment not found with ID");
        }
        newzyComment.setIsDeleted(true);
        save(newzyComment);
   }

}
