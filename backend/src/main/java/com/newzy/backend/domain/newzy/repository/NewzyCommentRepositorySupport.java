package com.newzy.backend.domain.newzy.repository;

import com.newzy.backend.domain.newzy.dto.response.NewzyCommentListGetResponseDto;
import com.newzy.backend.domain.newzy.entity.NewzyComment;
import com.newzy.backend.domain.newzy.entity.QNewzyComment;
import com.newzy.backend.domain.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class NewzyCommentRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;
    private final int size = 10;

    public NewzyCommentRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(NewzyComment.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Map<String, Object> findCommentList(int page, int size, Long newzyId) {
        QNewzyComment qNewzyComment = QNewzyComment.newzyComment1;  // QNewzyComment 인스턴스 / newzyComment와 충돌해서 1이 붙음
        QUser qUser = QUser.user;

        Long totalCount = jpaQueryFactory
                .select(qNewzyComment.count())
                .where(qNewzyComment.isDeleted.eq(false))
                .fetchOne();

        int totalPage = (int) ((totalCount + size - 1) / size);

        List<NewzyCommentListGetResponseDto> commentList = jpaQueryFactory
                .select(Projections.constructor(NewzyCommentListGetResponseDto.class,
                        qUser.userId,
                        qUser.email,
                        qUser.nickname,
                        qUser.image,
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
                .orderBy(qNewzyComment.createdAt.desc())
                .offset((page - 1) * size)
                .limit(size)
                .fetch();

        Map<String, Object> result = new HashMap<>();
        result.put("newzyCommentList", commentList); // list
        result.put("totalPage", totalPage); // int

        return result;
    }

}
