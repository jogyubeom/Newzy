    package com.newzy.backend.domain.newzy.entity;

    import com.fasterxml.jackson.annotation.JsonIgnore;
    import com.newzy.backend.domain.newzy.dto.request.NewzyCommentRequestDTO;
    import com.newzy.backend.domain.newzy.repository.NewzyCommentRepository;
    import com.newzy.backend.domain.newzy.repository.NewzyRepository;
    import com.newzy.backend.global.model.BaseTimeEntity;
    import jakarta.persistence.*;
    import lombok.*;
    import org.hibernate.annotations.DynamicUpdate;

    import java.util.ArrayList;
    import java.util.List;

    @Entity
    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Table(name = "newzy_comment")
    public class NewzyComment extends BaseTimeEntity {

        // 뉴지 댓글
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long newzyCommentId;

        // TODO: 추후 다시 수정 (newzyComment entity)
    //    @ManyToOne(fetch = FetchType.LAZY)
    //    @JoinColumn(name = "user_id", updatable = false)
    //    private User user;

        // 뉴지 아이디
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name="newzy_id", updatable = false, nullable = false)
        private Newzy newzy;

        // 부모 유저 아이디
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parent_comment_id")
        private NewzyComment parentComment;

        // 댓글 내용
        @Column(name = "newzy_comment")
        private String newzyComment;

        @Column(name = "is_updated")
        private Boolean isUpdated = false;

        @Column(name = "is_deleted")
        private Boolean isDeleted = false;

        public static NewzyComment convertToEntityByNewzyCommentId(Long newzyCommentId, NewzyCommentRequestDTO requestDTO){
            NewzyComment newzyComment = new NewzyComment();
            newzyComment.setNewzyCommentId(newzyCommentId);
            newzyComment.setNewzyComment(requestDTO.getNewzyComment());

            return newzyComment;
        }

        public static NewzyComment convertToEntityByNewzyId(NewzyCommentRequestDTO dto, Newzy newzy) {

            NewzyComment newzyComment = new NewzyComment();
            newzyComment.setNewzyComment(dto.getNewzyComment());
            newzyComment.setNewzy(newzy);
            newzyComment.setNewzyCommentId(dto.getNewzyCommentId());

            NewzyComment parentComment = new NewzyComment();

            if (dto.getNewzyCommentId() != null) {
                newzyComment.setNewzyCommentId(dto.getNewzyCommentId());
            }

            if (dto.getNewzyParentCommentId() != null) {
                parentComment.setNewzyCommentId(dto.getNewzyParentCommentId());
                newzyComment.setParentComment(parentComment);
            }

            return newzyComment;
        }

    }
