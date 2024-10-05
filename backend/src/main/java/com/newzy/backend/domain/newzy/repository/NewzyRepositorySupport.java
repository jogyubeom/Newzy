package com.newzy.backend.domain.newzy.repository;

import com.newzy.backend.domain.newzy.dto.response.NewzyListGetResponseDTO;
import com.newzy.backend.domain.newzy.entity.Newzy;
import com.newzy.backend.domain.newzy.entity.QNewzy;
import com.newzy.backend.domain.user.entity.QFollow;
import com.newzy.backend.domain.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        BooleanBuilder builder = new BooleanBuilder();

        // 기본 조건: 삭제되지 않은 뉴지 필터링
        builder.and(qnewzy.isDeleted.eq(false));

        // 카테고리 필터 조건
        if (category >= 0 && category <= 2) {
            builder.and(qnewzy.category.eq(category));
        }

        // 키워드 검색 조건 추가
        if (keyword != null && !keyword.isEmpty()) {
            builder.and(qnewzy.title.contains(keyword));
        }

        // JPAQuery 객체 초기화
        JPAQuery<Newzy> query;

        // userId가 0이 아닐 경우 팔로우한 사람의 뉴지를 조회하기 위해 조인 추가
        if (userId != 0) {
            query = queryFactory
                    .selectFrom(qnewzy)
                    .leftJoin(qnewzy.user, qUser)  // Newzy의 작성자와 Join
                    .leftJoin(qFollow).on(qFollow.toUser.eq(qUser))  // Follow와 Join
                    .where(qFollow.fromUser.userId.eq(userId));  // fromUser가 현재 userId와 일치하는 경우 필터링
        } else {
            // userId가 0인 경우 기본 쿼리만 수행
            query = queryFactory
                    .selectFrom(qnewzy)
                    .leftJoin(qnewzy.user, qUser);  // Newzy의 User와만 Join
        }

        // 전체 개수 조회
        Long totalCount = query
                .where(builder)  // 동적 조건 적용
                .fetchCount();

        // 총 페이지 수 계산
        int totalPage = (int) ((totalCount + size - 1) / size);

        // 정렬 조건 설정
        OrderSpecifier<?> orderSpecifier = qnewzy.createdAt.asc();  // 기본값: 오래된 순
        switch (sort) {
            case 0:  // 오래된 순
                orderSpecifier = qnewzy.createdAt.asc();
                break;
            case 1:  // 조회수 순
                orderSpecifier = qnewzy.hit.desc();
                break;
            case 2:  // 내가 팔로우한 사람들의 최신 뉴지 목록
                orderSpecifier = qnewzy.createdAt.desc();
                break;
        }

        // Newzy 목록 조회 - Projections.constructor 사용
        List<NewzyListGetResponseDTO> newzyList = query
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
                .where(builder)  // 최종 필터 조건 적용
                .orderBy(orderSpecifier)  // 정렬 조건 적용
                .offset((page - 1) * size)  // 페이지네이션 offset
                .limit(size)  // 페이지네이션 limit
                .fetch();

        // 결과를 Map으로 반환
        Map<String, Object> result = new HashMap<>();
        result.put("newzyList", newzyList);  // Newzy 목록
        result.put("totalPage", totalPage);  // 총 페이지 수

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
