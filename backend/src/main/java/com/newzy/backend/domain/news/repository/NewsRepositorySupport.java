package com.newzy.backend.domain.news.repository;

import com.newzy.backend.domain.news.dto.response.NewsDetailGetResponseDto;
import com.newzy.backend.domain.news.dto.response.NewsListGetResponseDto;
import com.newzy.backend.domain.news.entity.News;
import com.newzy.backend.domain.news.entity.QNews;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class NewsRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;
    private final int size = 10;


    public NewsRepositorySupport(JPAQueryFactory queryFactory) {
        super(News.class);
        this.queryFactory = queryFactory;
    }


    public Map<String, Object> findNewsList(int page, int sort, int category, String keyword) {
        QNews qNews = QNews.news;

        // 기본 where 조건을 생성
        BooleanBuilder whereCondition = new BooleanBuilder();

        // category가 3이 아닌 경우에만 카테고리 필터 추가
        if (category != 3) {
            whereCondition.and(qNews.category.eq(category));
        }

        // keyword가 null이 아니고 빈 문자열이 아닌 경우에만 타이틀 필터 추가
        if (keyword != null && !keyword.isEmpty()) {
            whereCondition.and(qNews.title.contains(keyword));
        }

        // 전체 뉴스 개수 조회
        Long totalCount = queryFactory
                .select(qNews.count())
                .from(qNews)
                .where(whereCondition)
                .fetchOne();

        // 총 페이지 수 계산 (마지막 페이지 번호)
        int totalPage = (int) ((totalCount + size - 1) / size);

        // 정렬 기준 설정
        OrderSpecifier<?> orderSpecifier;
        switch (sort) {
            case 1:
                orderSpecifier = qNews.newsId.asc(); // 오래된 순
                break;
            case 2:
                orderSpecifier = qNews.hit.desc(); // 조회수 순
                break;
            default:
                orderSpecifier = qNews.newsId.desc(); // 최신순
        }
        // 뉴스 목록 조회
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
                .from(qNews)
                .where(whereCondition)
                .orderBy(orderSpecifier)
                .offset((page - 1) * size)
                .limit(size)
                .fetch();

        // 결과를 Map에 담아 반환
        Map<String, Object> result = new HashMap<>();
        result.put("newsList", newsList); // list
        result.put("totalPage", totalPage); // int

        return result;
    }




    public NewsDetailGetResponseDto getNewsDetail(Long newsId) {
        QNews qNews = QNews.news;

        return (NewsDetailGetResponseDto) queryFactory
                .select(Projections.constructor(NewsDetailGetResponseDto.class,
                        qNews.newsId,
                        qNews.link,
                        qNews.title,
                        qNews.content,
                        qNews.contentText,
                        qNews.difficulty,
                        qNews.category,
                        qNews.publisher,
                        qNews.createdAt,
                        qNews.updatedAt,
                        qNews.crawledAt,
                        qNews.hit,
                        qNews.thumbnail
                ))
                .from(qNews)
                .where(qNews.newsId.eq(newsId))
                .fetchOne();
    }

    public List<NewsListGetResponseDto> findTop3NewsByDayWithHighestHits(LocalDateTime startOfDay, LocalDateTime now) {
        QNews qNews = QNews.news;

        return queryFactory
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
                .from(qNews)
                .where(qNews.createdAt.goe(startOfDay)) // 하루 시작 시점부터 현재 시간까지
                .orderBy(qNews.hit.desc()) // 조회수 내림차순
                .limit(3) // 상위 3개만
                .fetch();
    }
}


