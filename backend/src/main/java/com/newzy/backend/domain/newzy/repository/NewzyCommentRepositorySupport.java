package com.newzy.backend.domain.newzy.repository;

import com.newzy.backend.domain.newzy.dto.response.NewzyCommentListGetResponseDTO;
import com.newzy.backend.domain.newzy.entity.NewzyComment;
import com.newzy.backend.domain.newzy.entity.QNewzyComment;
import com.newzy.backend.domain.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NewzyCommentRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public NewzyCommentRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(NewzyComment.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<NewzyCommentListGetResponseDTO> findCommentList(Long newzyId) {
        QNewzyComment qNewzyComment = QNewzyComment.newzyComment1;  // QNewzyComment 인스턴스 / newzyComment와 충돌해서 1이 붙음
        QUser qUser = QUser.user;

        List<NewzyCommentListGetResponseDTO> commentList = jpaQueryFactory
                .select(Projections.constructor(NewzyCommentListGetResponseDTO.class,
                        qUser.userId,
                        qUser.email,
                        qUser.nickname,
                        qUser.image.imageUrl,
                        qNewzyComment.newzyCommentId,
                        qNewzyComment.newzyComment,
                        qNewzyComment.createdAt,
                        qNewzyComment.newzy.newzyId,
                        qNewzyComment.parentComment.newzyCommentId
                ))
                .from(qNewzyComment)
                .join(qNewzyComment.user, qUser)
                .where(qNewzyComment.newzy.newzyId.eq(newzyId)
                        .and(qNewzyComment.isDeleted.eq(false)))
                .orderBy(qNewzyComment.createdAt.asc())
                .fetch();

        return commentList;
    }

}
