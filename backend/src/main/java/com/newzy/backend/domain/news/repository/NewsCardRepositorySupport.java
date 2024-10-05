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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class NewsCardRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public NewsCardRepositorySupport(JPAQueryFactory queryFactory) {
        super(NewsCard.class);
        this.queryFactory = queryFactory;
    }

    public Map<String, Object> findNewsCardList(Long userId, int page) {
        QNewsCard newsCard = QNewsCard.newsCard;
        QUser user = QUser.user;
        QNews news = QNews.news;

        // 페이지 크기 설정
        int size = 8;

        // 전체 개수 조회
        long totalCount = queryFactory
                .select(newsCard.count())
                .from(newsCard)
                .where(newsCard.user.userId.eq(userId))
                .fetchOne();

        // 총 페이지 수 계산
        int totalPage = (int) ((totalCount + size - 1) / size);

        // NewsCard 목록 조회 - 페이지네이션 적용
        List<NewsCardListGetResponseDto> newsCardList = queryFactory
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
                .offset((page - 1) * size)  // 페이지네이션 offset
                .limit(size)  // 페이지네이션 limit
                .fetch();

        // 결과를 Map으로 반환
        Map<String, Object> result = new HashMap<>();
        result.put("newsCardList", newsCardList);  // NewsCard 목록
        result.put("totalPage", totalPage);  // 총 페이지 수

        return result;
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
