package com.newzy.backend.domain.news.repository;

import com.newzy.backend.domain.news.dto.response.NewsListGetResponseDto;
import com.newzy.backend.domain.news.entity.News;
import com.newzy.backend.domain.news.entity.QNews;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

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

}
