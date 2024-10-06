package com.newzy.backend.domain.newzy.repository;

import com.newzy.backend.domain.newzy.dto.response.NewzyListGetResponseDTO;
import com.newzy.backend.domain.newzy.entity.NewzyLike;
import com.newzy.backend.domain.newzy.entity.QNewzy;
import com.newzy.backend.domain.newzy.entity.QNewzyLike;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class NewzyLikeRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;
    private final int size = 10;

    public NewzyLikeRepositorySupport(JPAQueryFactory queryFactory) {
        super(NewzyLike.class);
        this.queryFactory = queryFactory;
    }

    public Map<String, Object> findNewzyListByNewzyLike(int page, Long userId) {
        QNewzyLike qNewzyLike = QNewzyLike.newzyLike;
        QNewzy qNewzy = QNewzy.newzy;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qNewzyLike.user.userId.eq(userId));

        Long totalCount = queryFactory
                .select(qNewzyLike.count())
                .from(qNewzyLike)
                .where(builder)
                .fetchOne();

        // 총 페이지 수 계산 (마지막 페이지 번호)
        int totalPage = (int) ((totalCount + size - 1) / size);

        List<NewzyListGetResponseDTO> newzyList = queryFactory
                .select(Projections.constructor(NewzyListGetResponseDTO.class,
                        qNewzy.newzyId,
                        qNewzy.user.nickname,
                        qNewzy.user.userId,
                        qNewzy.user.image.imageUrl,
                        qNewzy.title,
                        qNewzy.content,
                        qNewzy.contentText,
                        qNewzy.category,
                        qNewzy.likeCnt,
                        qNewzy.thumbnail,
                        qNewzy.hit,
                        qNewzy.createdAt,
                        qNewzy.updatedAt
                ))
                .from(qNewzyLike)
                .join(qNewzyLike.newzy, qNewzy)
                .where(builder)
                .orderBy(qNewzyLike.createdAt.desc())
                .offset((page - 1) * size)
                .limit(size)
                .fetch();

        Map<String, Object> result = new HashMap<>();
        result.put("newzyList", newzyList);  // 뉴스 목록
        result.put("totalPage", totalPage);  // 전체 페이지 수

        return result;

    }
}

