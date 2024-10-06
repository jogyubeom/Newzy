package com.newzy.scheudling.domain.card.repository;

import com.newzy.scheudling.domain.card.dto.CardCountDTO;
import com.newzy.scheudling.domain.card.entity.NewsCard;
import com.newzy.scheudling.domain.card.entity.QNewsCard;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class NewsCardRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public NewsCardRepositorySupport(JPAQueryFactory queryFactory) {
        super(NewsCard.class);
        this.queryFactory = queryFactory;
    }

    public Optional<CardCountDTO> findTopCollectorUserId(LocalDateTime startDate, LocalDateTime endDate) {
        QNewsCard newsCard = QNewsCard.newsCard;

        CardCountDTO result = queryFactory
                .select(Projections.constructor(CardCountDTO.class,
                        newsCard.user.userId,
                        newsCard.user.userId.count().as("count")))
                .from(newsCard)
                .where(newsCard.createdAt.between(startDate, endDate))
                .groupBy(newsCard.user.userId)
                .orderBy(newsCard.user.userId.count().desc())
                .fetchFirst();

        return Optional.ofNullable(result);
    }
}