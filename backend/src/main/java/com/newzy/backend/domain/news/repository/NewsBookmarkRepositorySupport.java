package com.newzy.backend.domain.news.repository;

import com.newzy.backend.domain.news.dto.response.NewsListGetResponseDto;
import com.newzy.backend.domain.news.entity.NewsBookmark;
import com.newzy.backend.domain.news.entity.QNews;
import com.newzy.backend.domain.news.entity.QNewsBookmark;
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
public class NewsBookmarkRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;
    private final int size = 10;

    public NewsBookmarkRepositorySupport(JPAQueryFactory queryFactory) {
        super(NewsBookmark.class);
        this.queryFactory = queryFactory;
    }

    public Map<String, Object> findNewsListByNewsBookmark(int page, Long userId) {
        QNewsBookmark qNewsBookmark = QNewsBookmark.newsBookmark;
        QNews qNews = QNews.news;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qNewsBookmark.user.userId.eq(userId));


        Long totalCount = queryFactory
                .select(qNewsBookmark.count())
                .from(qNewsBookmark)
                .where(builder)
                .fetchOne();

        // 총 페이지 수 계산 (마지막 페이지 번호)
        int totalPage = (int) ((totalCount + size - 1) / size);

        List<NewsListGetResponseDto> newsList = queryFactory
                .select(Projections.constructor(NewsListGetResponseDto.class,
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
                .from(qNewsBookmark)
                .join(qNewsBookmark.news, qNews)
                .where(builder)
                .orderBy(qNewsBookmark.createdAt.desc())
                .offset((page - 1) * size)
                .limit(size)
                .fetch();

        Map<String, Object> result = new HashMap<>();
        result.put("newsList", newsList);  // 뉴스 목록
        result.put("totalPage", totalPage);  // 전체 페이지 수

        return result;
    }
}
