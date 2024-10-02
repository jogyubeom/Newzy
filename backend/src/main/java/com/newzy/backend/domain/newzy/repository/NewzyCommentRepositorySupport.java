package com.newzy.backend.domain.newzy.repository;

import com.newzy.backend.domain.newzy.dto.response.NewzyCommentListGetResponseDto;
import com.newzy.backend.domain.newzy.entity.NewzyComment;
import com.newzy.backend.domain.newzy.entity.QNewzyComment;
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

        Long totalCount = jpaQueryFactory
                .select(qNewzyComment.count())
                .where(qNewzyComment.isDeleted.eq(false))
                .fetchOne();

        int totalPage = (int) ((totalCount + size - 1) / size);

        List<NewzyCommentListGetResponseDto> commentList = jpaQueryFactory
                .select(Projections.constructor(NewzyCommentListGetResponseDto.class,
                        qNewzyComment.newzyCommentId,
                        qNewzyComment.newzyComment,
                        qNewzyComment.createdAt,
                        qNewzyComment.newzy.newzyId,
                        qNewzyComment.parentComment.newzyCommentId
                ))
                .from(qNewzyComment)
                .where(
                        qNewzyComment.newzy.newzyId.eq(newzyId)
                                .and(qNewzyComment.isDeleted.eq(false))
                )
                .offset(page * size)
                .limit(size)
                .fetch();

        Map<String, Object> result = new HashMap<>();
        result.put("newsList", commentList); // list
        result.put("totalPage", totalPage); // int

        return result;
    }

}
