package com.newzy.backend.domain.news.repository;

import com.newzy.backend.domain.news.dto.response.NewsListGetResponseDto;
import com.newzy.backend.domain.news.entity.News;
import com.newzy.backend.domain.news.entity.QNews;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class NewsRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;
    private final int size = 10;


    public NewsRepositorySupport(JPAQueryFactory queryFactory) {
        super(News.class);
        this.queryFactory = queryFactory;
    }


    public List<NewsListGetResponseDto> findNewsList(int page, int category) {
        QNews qNews = QNews.news;

        return queryFactory
                .select(Projections.constructor(NewsListGetResponseDto.class,
                        qNews.newsId,
                        qNews.link,
                        qNews.title,
                        qNews.content,
                        qNews.category,
                        qNews.publisher,
                        qNews.hit,
                        qNews.createdAt
                ))
                .from(qNews)
                .where(category != 3 ? qNews.category.eq(category) : null)
                .offset(page * size)
                .limit(size)
                .fetch();
    }

    public List<NewsListGetResponseDto> findTop3NewsByDayWithHighestHits(LocalDateTime startOfDay) {
        QNews qNews = QNews.news;

        return queryFactory
                .select(Projections.constructor(NewsListGetResponseDto.class,

                        qNews.newsId,
                        qNews.link,
                        qNews.title,
                        qNews.content,
                        qNews.category,
                        qNews.publisher,
                        qNews.hit,
                        qNews.createdAt
                ))
                .from(qNews)
                .where(qNews.createdAt.goe(startOfDay)) // 하루 시작 시점부터 현재 시간까지
                .orderBy(qNews.hit.desc()) // 조회수 내림차순
                .limit(3) // 상위 3개만
                .fetch();
    }
}


