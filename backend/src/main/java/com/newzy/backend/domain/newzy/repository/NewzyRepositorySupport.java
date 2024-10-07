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
        QImage qImage = QImage.image;

        // BooleanBuilder를 사용하여 필터 조건 설정
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

        // JPAQuery 객체 생성 및 Join 설정
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
                .leftJoin(qUser.image, qImage)
                .where(builder);  // 최종 조건 적용

        // 정렬 조건 설정
        if (sort == 1) {
            query.orderBy(qnewzy.hit.desc());  // 조회수 순 정렬
            log.info(">>> 정렬 조건: 조회수 순");
        } else {
            query.orderBy(qnewzy.createdAt.desc());  // 기본값: 최신 순 정렬
            log.info(">>> 정렬 조건: 최신 순");
        }

        // 페이징 및 데이터 조회
        List<NewzyListGetResponseDTO> newzyList = query
                .offset((page - 1) * size)
                .limit(size)
                .fetch();

        log.info(">>> findNewzyList - 조회된 Newzy 목록 개수: {}", newzyList.size());

        // 전체 개수 조회 및 페이지 수 계산
        Long totalCount = query.fetchCount();
        int totalPage = (int) ((totalCount + size - 1) / size);

        // 결과를 Map으로 반환
        Map<String, Object> result = new HashMap<>();
        result.put("newzyList", newzyList);  // Newzy 목록
        result.put("totalPage", totalPage);  // 총 페이지 수

        return result;
    }


    public Map<String, Object> getNewzyListByNickname(int page, String nickname) {
        QNewzy qnewzy = QNewzy.newzy;
        QUser qUser = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qnewzy.isDeleted.eq(false));
        builder.and(qnewzy.user.nickname.eq(nickname));

        Long totalCount = queryFactory
                .select(qnewzy.count())
                .from(qnewzy)
                .where(builder)  // 동적 조건
                .fetchOne();

        int totalPage = (int) ((totalCount + size - 1) / size);

        List<NewzyListGetResponseDTO> myNewzyList = queryFactory
                .select(Projections.fields(NewzyListGetResponseDTO.class,
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
        result.put("newzyList", myNewzyList); // list
        result.put("totalPage", totalPage); // int

        return result;
    }


    public Map<String, Object> findFollowingNewzyList(int page, int category, String keyword, int sort, Long userId) {
        QFollow qFollow = QFollow.follow;
        QNewzy qNewzy = QNewzy.newzy;
        QUser qUser = QUser.user;
        QImage qImage = QImage.image;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qFollow.fromUser.userId.eq(userId));
        builder.and(qNewzy.isDeleted.eq(false));

        // 2. 카테고리 필터 조건 추가
        if (category >= 0 && category <= 2) {
            builder.and(qNewzy.category.eq(category));
            log.info(">>> 카테고리 필터 적용: category = {}", category);
        } else {
            log.warn(">>> 잘못된 카테고리 값: {}", category);
        }

        // 3. 키워드 검색 조건 추가
        if (keyword != null && !keyword.isEmpty()) {
            builder.and(qNewzy.title.contains(keyword));
        }

        JPAQuery<NewzyListGetResponseDTO> query = queryFactory
                .select(Projections.fields(NewzyListGetResponseDTO.class,
                        qNewzy.user.userId,
                        qNewzy.user.nickname,
                        qNewzy.user.email,
                        qNewzy.user.image.imageUrl,
                        qNewzy.newzyId,
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
                .from(qFollow)
                .leftJoin(qFollow.toUser, qUser)  // Follow 테이블에서 toUser와 Join
                .leftJoin(qNewzy).on(qNewzy.user.eq(qUser))  // Follow의 toUser가 작성한 Newzy와 Join
                .leftJoin(qUser.image, qImage)    // `User`와 연결된 `Image`와 Join
                .where(builder);

        if (sort == 1) {
            query.orderBy(qNewzy.hit.desc());  // 조회수 순 정렬
            log.info(">>> 정렬 조건: 조회수 순");
        } else {
            query.orderBy(qNewzy.createdAt.desc());  // 기본값: 최신 순 정렬
            log.info(">>> 정렬 조건: 최신 순");
        }

        // 6. 페이징 및 데이터 조회
        List<NewzyListGetResponseDTO> followerNewzyList = query
                .offset((page - 1) * size)
                .limit(size)
                .fetch();

        log.info(">>> findFollowingNewzyList - 조회된 Newzy 목록 개수: {}", followerNewzyList.size());

        // 7. 전체 개수 조회 및 페이지 수 계산
        Long totalCount = query.fetchCount();
        int totalPage = (int) ((totalCount + size - 1) / size);

        // 8. 결과를 Map으로 반환
        Map<String, Object> result = new HashMap<>();
        result.put("newzyList", followerNewzyList);  // Newzy 목록
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
