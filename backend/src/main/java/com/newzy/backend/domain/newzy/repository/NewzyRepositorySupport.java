package com.newzy.backend.domain.newzy.repository;

import com.newzy.backend.domain.newzy.dto.response.NewzyListGetResponseDTO;
import com.newzy.backend.domain.newzy.entity.Newzy;
import com.newzy.backend.domain.newzy.entity.QNewzy;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class NewzyRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;
    private final int size = 10;

    public NewzyRepositorySupport(JPAQueryFactory queryFactory) {
        super(Newzy.class);
        this.queryFactory = queryFactory;
    }

    public Map<String, Object> findNewzyList(int page, int category, String keyword) {
        QNewzy qnewzy = QNewzy.newzy;

        BooleanBuilder builder = new BooleanBuilder();

        // isDeleted가 false인 경우만 필터링
        builder.and(qnewzy.isDeleted.eq(false));

        // 카테고리 조건 (0~2인 경우에만 적용)
        if (category >= 0 && category <= 2) {
            builder.and(qnewzy.category.eq(category));
        }

        // keyword가 null이 아니고 빈 문자열이 아닌 경우에만 타이틀 필터 추가
        if (keyword != null && !keyword.isEmpty()) {
            builder.and(qnewzy.title.contains(keyword));
        }

        Long totalCount = queryFactory
                .select(qnewzy.count())
                .from(qnewzy)
                .where(builder)  // 동적 조건
                .fetchOne();

        int totalPage = (int) ((totalCount + size - 1) / size);

        List<NewzyListGetResponseDTO> newzyList = queryFactory
                .select(Projections.fields(NewzyListGetResponseDTO.class,
                        qnewzy.newzyId,
                        qnewzy.title,
                        qnewzy.content,
                        qnewzy.category,
                        qnewzy.createdAt,
                        qnewzy.likeCnt,
                        qnewzy.visitCnt
                ))
                .from(qnewzy)
                .where(builder)  // 동적 조건
                .orderBy(qnewzy.newzyId.desc())
                .offset((page-1) * size)
                .limit(size)
                .fetch();

        Map<String, Object> result = new HashMap<>();
        result.put("newzyList", newzyList); // list
        result.put("totalPage", totalPage); // int

        return result;
    }
}
