package com.newzy.backend.domain.news.repository;

import com.newzy.backend.domain.news.dto.response.NewsListGetResponseDTO;
import com.newzy.backend.domain.news.entity.NewsLike;
import com.newzy.backend.domain.news.entity.QNews;
import com.newzy.backend.domain.news.entity.QNewsLike;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class NewsLikeRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;
    private final int size = 10;

    public NewsLikeRepositorySupport(JPAQueryFactory queryFactory) {
        super(NewsLike.class);
        this.queryFactory = queryFactory;
    }

    public Map<String, Object> findNewsListByNewsLike(int page, Long userId) {
        QNewsLike qNewsLike = QNewsLike.newsLike;
        QNews qNews = QNews.news;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qNewsLike.user.userId.eq(userId));

        Long totalCount = queryFactory
                .select(qNewsLike.count())
                .from(qNewsLike)
                .where(builder)
                .fetchOne();

        // 총 페이지 수 계산 (마지막 페이지 번호)
        int totalPage = (int) ((totalCount + size - 1) / size);

        List<NewsListGetResponseDTO> newsList = queryFactory
                .select(Projections.constructor(NewsListGetResponseDTO.class,
                        qNews.newsId,
                        qNews.link,
                        qNews.title,
                        qNews.contentText,
                        qNews.category,
                        qNews.publisher,
                        qNews.thumbnail,
                        qNews.hit,
                        qNews.createdAt
                ))
                .from(qNewsLike)
                .join(qNewsLike.news, qNews)
                .where(builder)
                .orderBy(qNewsLike.createdAt.desc())
                .offset((page - 1) * size)
                .limit(size)
                .fetch();

        Map<String, Object> result = new HashMap<>();
        result.put("newsList", newsList);  // 뉴스 목록
        result.put("totalPage", totalPage);  // 전체 페이지 수

        return result;
    }
}
