package com.newzy.backend.domain.newzy.repository;

import com.newzy.backend.domain.newzy.dto.response.NewzyListGetResponseDTO;
import com.newzy.backend.domain.newzy.entity.Newzy;
import com.newzy.backend.domain.newzy.entity.QNewzy;
import com.newzy.backend.domain.user.entity.QUser;
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
        QUser qUser = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qnewzy.isDeleted.eq(false));

        if (category >= 0 && category <= 2) {
            builder.and(qnewzy.category.eq(category));
        }

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
                .select(Projections.constructor(NewzyListGetResponseDTO.class,
                        qnewzy.user.userId,
                        qnewzy.user.nickname,
                        qnewzy.user.email,
                        qnewzy.user.image,
                        qnewzy.newzyId,
                        qnewzy.title,
                        qnewzy.content,
                        qnewzy.contentText,
                        qnewzy.category,
                        qnewzy.likeCnt,
                        qnewzy.thumbnail,
                        qnewzy.hit,
                        qnewzy.createdAt,
                        qnewzy.updatedAt
                ))
                .from(qnewzy)
                .join(qnewzy.user, qUser)
                .where(builder)  // 동적 조건 적용
                .orderBy(qnewzy.createdAt.desc())
                .offset((page - 1) * size)
                .limit(size)
                .fetch();

        Map<String, Object> result = new HashMap<>();
        result.put("newzyList", newzyList); // list
        result.put("totalPage", totalPage); // int

        return result;
    }
}
