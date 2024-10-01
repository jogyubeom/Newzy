package com.newzy.backend.domain.newzy.repository;

import com.newzy.backend.domain.newzy.dto.response.NewzyListGetResponseDTO;
import com.newzy.backend.domain.newzy.entity.Newzy;
import com.newzy.backend.domain.newzy.entity.QNewzy;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NewzyRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;
    private final int size = 10;

    public NewzyRepositorySupport(JPAQueryFactory queryFactory) {
        super(Newzy.class);
        this.queryFactory = queryFactory;
    }

    public List<NewzyListGetResponseDTO> findNewzyList(int page, int size, int category) {
        QNewzy qnewzy = QNewzy.newzy;

        BooleanBuilder whereClause = new BooleanBuilder();

        // isDeleted가 false인 경우만 필터링
        whereClause.and(qnewzy.isDeleted.eq(false));

        // 카테고리 조건 (0~2인 경우에만 적용)
        if (category >= 0 && category <= 2) {
            whereClause.and(qnewzy.category.eq(category));
        }

        return queryFactory
                .select(Projections.fields(NewzyListGetResponseDTO.class,
                        qnewzy.newzyId.as("newzyId"),
                        qnewzy.title.as("title"),
                        qnewzy.content.as("content"),
                        qnewzy.category.as("category"),
                        qnewzy.createdAt.as("createdAt"),
                        qnewzy.likeCnt.as("likeCnt"),
                        qnewzy.visitCnt.as("visitCnt")
                ))
                .from(qnewzy)
                .where(whereClause)  // 동적 조건
                .offset(page * size)
                .limit(size)
                .fetch();
    }
}
