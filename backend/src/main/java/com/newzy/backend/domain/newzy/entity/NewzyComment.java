    package com.newzy.backend.domain.newzy.entity;

    import com.newzy.backend.domain.newzy.dto.request.NewzyCommentRequestDTO;
    import com.newzy.backend.domain.user.entity.User;
    import com.newzy.backend.global.exception.StringLengthLimitException;
    import com.newzy.backend.global.model.BaseTimeEntity;
    import jakarta.persistence.*;
    import lombok.*;

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

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", updatable = false)
        private User user;

        // 뉴지 아이디
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name="newzy_id", updatable = false, nullable = false)
        private Newzy newzy;

        // 부모 유저 아이디
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parent_comment_id")
        private NewzyComment parentComment;

        // 댓글 내용
        @Column(name = "newzy_comment", columnDefinition = "TEXT", length = 500)
        private String newzyComment;

        @Column(name = "is_updated")
        private Boolean isUpdated = false;

        @Column(name = "is_deleted")
        private Boolean isDeleted = false;

        public static NewzyComment convertToEntityByNewzyCommentId(User user, Long newzyCommentId, NewzyCommentRequestDTO requestDTO){

            if (requestDTO.getNewzyComment().length() > 500) {
                throw new StringLengthLimitException("댓글은 최대 500자까지 입력할 수 있습니다.");
            }
            NewzyComment newzyComment = new NewzyComment();
            newzyComment.setUser(user);
            newzyComment.setNewzyCommentId(newzyCommentId);
            newzyComment.setNewzyComment(requestDTO.getNewzyComment());

            return newzyComment;
        }

        public static NewzyComment convertToEntityByNewzyId(NewzyCommentRequestDTO dto, User user, Newzy newzy) {

            if (dto.getNewzyComment().length() > 500) {
                throw new StringLengthLimitException("댓글은 최대 500자까지 입력할 수 있습니다.");
            }
            NewzyComment newzyComment = new NewzyComment();
            newzyComment.setNewzyComment(dto.getNewzyComment());
            newzyComment.setUser(user);
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
