package com.newzy.backend.domain.newzy.repository;

import com.newzy.backend.domain.image.entity.QImage;
import com.newzy.backend.domain.newzy.dto.response.NewzyListGetResponseDTO;
import com.newzy.backend.domain.newzy.entity.Newzy;
import com.newzy.backend.domain.newzy.entity.QNewzy;
import com.newzy.backend.domain.user.entity.QFollow;
import com.newzy.backend.domain.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class NewzyRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;
    private final int size = 10;


    public NewzyRepositorySupport(JPAQueryFactory queryFactory) {
        super(Newzy.class);
        this.queryFactory = queryFactory;
    }

    public Map<String, Object> findNewzyList(int page, int category, String keyword, int sort, Long userId) {
        QNewzy qnewzy = QNewzy.newzy;
        QUser qUser = QUser.user;
        QFollow qFollow = QFollow.follow;
        QImage qImage = QImage.image;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qnewzy.isDeleted.eq(false));  // 기본 조건: 삭제되지 않은 뉴지 필터링

        // 카테고리 필터 조건 추가
        if (category >= 0 && category <= 2) {
            builder.and(qnewzy.category.eq(category));
            log.info(">>> 카테고리 필터 적용: category = {}", category);
        } else {
            log.warn(">>> 잘못된 카테고리 값: {}", category);
        }

        // 키워드 검색 조건 추가
        if (keyword != null && !keyword.isEmpty()) {
            builder.and(qnewzy.title.contains(keyword));
        }

        if (sort == 2) {
            builder.and(qFollow.fromUser.userId.eq(userId));
        }

        JPAQuery<NewzyListGetResponseDTO> query = queryFactory
                .select(Projections.constructor(NewzyListGetResponseDTO.class,
                        qnewzy.user.userId,
                        qnewzy.user.nickname,
                        qnewzy.user.email,
                        qnewzy.user.image.imageUrl,
                        qnewzy.newzyId,
                        qnewzy.title,
                        qnewzy.content,
                        qnewzy.contentText,
                        qnewzy.category,
                        qnewzy.likeCnt,
                        qnewzy.thumbnail,
                        qnewzy.hit,
                        qnewzy.createdAt,
                        qnewzy.updatedAt
                ))
                .from(qnewzy)
                .leftJoin(qnewzy.user, qUser)
                .leftJoin(qFollow).on(qFollow.toUser.eq(qUser))
                .leftJoin(qUser.image, qImage)
                .where(builder)  // 최종 조건 적용
                .orderBy(qnewzy.createdAt.asc())  // 정렬 조건 설정
                .offset((page - 1) * size)
                .limit(size);

        List<NewzyListGetResponseDTO> newzyList = query.fetch();
        log.info(">>> findNewzyList - 조회된 Newzy 목록 개수: {}", newzyList.size());

        Long totalCount = query.fetchCount();
        int totalPage = (int) ((totalCount + size - 1) / size);

        Map<String, Object> result = new HashMap<>();
        result.put("newzyList", newzyList);
        result.put("totalPage", totalPage);

        return result;
    }





    public Map<String, Object> getMyNewzyList(int page, Long userId) {
        QNewzy qnewzy = QNewzy.newzy;
        QUser qUser = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qnewzy.isDeleted.eq(false));
        builder.and(qnewzy.user.userId.eq(userId));

        Long totalCount = queryFactory
                .select(qnewzy.count())
                .from(qnewzy)
                .where(builder)  // 동적 조건
                .fetchOne();

        int totalPage = (int) ((totalCount + size - 1) / size);

        List<NewzyListGetResponseDTO> myNewzyList = queryFactory
                .select(Projections.constructor(NewzyListGetResponseDTO.class,
                        qnewzy.user.userId,
                        qnewzy.user.nickname,
                        qnewzy.user.email,
                        qnewzy.user.image.imageUrl,
                        qnewzy.newzyId,
                        qnewzy.title,
                        qnewzy.content,
                        qnewzy.contentText,
                        qnewzy.category,
                        qnewzy.likeCnt,
                        qnewzy.thumbnail,
                        qnewzy.hit,
                        qnewzy.createdAt,
                        qnewzy.updatedAt
                ))
                .from(qnewzy)
                .join(qnewzy.user, qUser)
                .where(builder)  // 동적 조건 적용
                .orderBy(qnewzy.createdAt.desc())
                .offset((page - 1) * size)
                .limit(size)
                .fetch();

        Map<String, Object> result = new HashMap<>();
        result.put("myNewzyList", myNewzyList); // list
        result.put("totalPage", totalPage); // int

        return result;
    }
}
