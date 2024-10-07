package com.newzy.scheudling.domain.newzy.repository;

import com.newzy.scheudling.domain.newzy.entity.Newzy;
import com.newzy.scheudling.domain.newzy.entity.QNewzy;
import com.newzy.scheudling.domain.newzy.entity.QNewzyLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class NewzyRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;


    public NewzyRepositorySupport(JPAQueryFactory queryFactory) {
        super(Newzy.class);
        this.queryFactory = queryFactory;
    }

    public List<Newzy> findTop3ByLikesBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        QNewzy newzy = QNewzy.newzy;
        QNewzyLike newzyLike = QNewzyLike.newzyLike;

        return queryFactory.selectFrom(newzy)
                .join(newzyLike).on(newzy.newzyId.eq(newzyLike.newzy.newzyId))
                .where(newzy.createdAt.between(startDate, endDate))
                .groupBy(newzy.newzyId)
                .orderBy(newzy.likeCnt.desc())  // 좋아요 개수 내림차순 정렬
                .limit(3)  // 상위 3개
                .fetch();
    }

}