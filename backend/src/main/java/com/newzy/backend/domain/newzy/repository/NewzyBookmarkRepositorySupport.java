package com.newzy.backend.domain.newzy.repository;

import com.newzy.backend.domain.news.dto.response.NewsListGetResponseDto;
import com.newzy.backend.domain.news.entity.QNews;
import com.newzy.backend.domain.news.entity.QNewsBookmark;
import com.newzy.backend.domain.newzy.dto.response.NewzyListGetResponseDTO;
import com.newzy.backend.domain.newzy.entity.NewzyBookmark;
import com.newzy.backend.domain.newzy.entity.QNewzy;
import com.newzy.backend.domain.newzy.entity.QNewzyBookmark;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class NewzyBookmarkRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;
    private final int size = 10;

    public NewzyBookmarkRepositorySupport(JPAQueryFactory queryFactory) {
        super(NewzyBookmark.class);
        this.queryFactory = queryFactory;
    }

    public Map<String, Object> findNewzyListByNewzyBookmark(int page, Long userId) {
        QNewzyBookmark qNewzyBookmark = QNewzyBookmark.newzyBookmark;
        QNewzy qNewzy = QNewzy.newzy;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qNewzyBookmark.user.userId.eq(userId));


        Long totalCount = queryFactory
                .select(qNewzyBookmark.count())
                .from(qNewzyBookmark)
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
                .from(qNewzyBookmark)
                .join(qNewzyBookmark.newzy, qNewzy)
                .where(builder)
                .orderBy(qNewzyBookmark.createdAt.desc())
                .offset((page - 1) * size)
                .limit(size)
                .fetch();

        Map<String, Object> result = new HashMap<>();
        result.put("newzyList", newzyList);  // 뉴지 목록
        result.put("totalPage", totalPage);  // 전체 페이지 수

        return result;
    }
}
