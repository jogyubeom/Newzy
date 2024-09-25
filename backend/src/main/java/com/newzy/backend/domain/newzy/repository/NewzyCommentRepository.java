package com.newzy.backend.domain.newzy.repository;
import com.newzy.backend.domain.newzy.entity.NewzyComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewzyCommentRepository extends JpaRepository<NewzyComment, Long> {

    // 뉴지 코멘트 찾기 -> newzyId + newzyCommentId
    // SELECT nc FROM NewzyComment nc WHERE nc.newzy.newzyId = :newzyId AND nc.newzyCommentId = :newzyCommentId AND nc.isDeleted = false
    NewzyComment findByNewzy_NewzyIdAndNewzyCommentIdAndIsDeletedFalse(Long newzyId, Long newzyCommentId);

    // SELECT nc FROM NewzyComment nc WHERE nc.newzy.newzyId = :newzyId AND nc.isDeleted = false
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
