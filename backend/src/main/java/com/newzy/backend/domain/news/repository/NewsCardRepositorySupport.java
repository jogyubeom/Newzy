package com.newzy.backend.domain.news.repository;

import com.newzy.backend.domain.news.dto.response.NewsCardListGetResponseDto;
import com.newzy.backend.domain.news.entity.NewsCard;
import com.newzy.backend.domain.news.entity.QNews;
import com.newzy.backend.domain.news.entity.QNewsCard;
import com.newzy.backend.domain.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NewsCardRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public NewsCardRepositorySupport(JPAQueryFactory queryFactory) {
        super(NewsCard.class);
        this.queryFactory = queryFactory;
    }

    public List<NewsCardListGetResponseDto> findNewsCardList(Long userId) {
        QNewsCard newsCard = QNewsCard.newsCard;
        QUser user = QUser.user;
        QNews news = QNews.news;

        return queryFactory
                .select(Projections.constructor(NewsCardListGetResponseDto.class,
                        newsCard.newsCardId,
                        newsCard.user.userId,
                        newsCard.user.nickname,
                        newsCard.news.newsId,
                        newsCard.news.title,
                        newsCard.summary,
                        newsCard.news.category,
                        newsCard.news.thumbnail
                ))
                .from(newsCard)
                .join(newsCard.user, user)
                .join(newsCard.news, news)
                .where(newsCard.user.userId.eq(userId))
                .fetch();
    }

    public NewsCardListGetResponseDto findNewsCardInfo(Long cardId) {
        QNewsCard newsCard = QNewsCard.newsCard;
        QUser user = QUser.user;
        QNews news = QNews.news;

        return queryFactory
                .select(Projections.constructor(NewsCardListGetResponseDto.class,
                        newsCard.newsCardId,
                        newsCard.user.userId,
                        newsCard.user.nickname,
                        newsCard.news.newsId,
                        newsCard.news.title,
                        newsCard.summary,
                        newsCard.news.category,
                        newsCard.news.thumbnail
                ))
                .from(newsCard)
                .join(newsCard.user, user)
                .join(newsCard.news, news)
                .where(newsCard.newsCardId.eq(cardId))
                .fetchOne();
    }
}
