package com.newzy.backend.domain.user.repository;

import com.newzy.backend.domain.newzy.dto.response.NewzyListGetResponseDTO;
import com.newzy.backend.domain.newzy.entity.QNewzy;
import com.newzy.backend.domain.user.dto.response.FollowListGetResponseDTO;
import com.newzy.backend.domain.user.entity.QFollow;
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
public class FollowRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;
    private final int size = 20;

    public FollowRepositorySupport(JPAQueryFactory queryFactory) {
        super(FollowRepository.class);
        this.queryFactory = queryFactory;
    }


    public Map<String, Object> findFollowingList(int page, String nickname) {
        QFollow qFollow = QFollow.follow;

        BooleanBuilder builder = new BooleanBuilder();

        if (nickname.equals(qFollow.fromUser.nickname)) {
            builder.and(qFollow.fromUser.nickname.eq(nickname));
        }

        Long totalCount = queryFactory
                .select(qFollow.count())
                .from(qFollow)
                .where(qFollow.fromUser.nickname.eq(nickname))
                .fetchCount();

        int totalPage = (int) ((totalCount + size - 1) / size);

        List<FollowListGetResponseDTO> followerList = queryFactory
                .select(Projections.constructor(FollowListGetResponseDTO.class,
                        qFollow.followId,
                        qFollow.fromUser.nickname,
                        qFollow.toUser.nickname
                ))
                .from(qFollow)
                .where(builder)
                .orderBy(qFollow.createdAt.desc())
                .offset((page - 1) * size)
                .limit(size)
                .fetch();

        Map<String, Object> result = new HashMap<>();
        result.put("followerList", followerList); // list
        result.put("totalPage", totalPage); // int

        return result;

    }

    public Map<String, Object> findFollowerList(int page, String nickname) {
        QFollow qFollow = QFollow.follow;

        BooleanBuilder builder = new BooleanBuilder();

        if (nickname.equals(qFollow.toUser.nickname)) {
            builder.and(qFollow.toUser.nickname.eq(nickname));
        }

        Long totalCount = queryFactory
                .select(qFollow.count())
                .from(qFollow)
                .where(qFollow.toUser.nickname.eq(nickname))
                .fetchCount();

        int totalPage = (int) ((totalCount + size - 1) / size);

        List<FollowListGetResponseDTO> followerList = queryFactory
                .select(Projections.constructor(FollowListGetResponseDTO.class,
                        qFollow.followId,
                        qFollow.fromUser.nickname,
                        qFollow.toUser.nickname
                ))
                .from(qFollow)
                .where(builder)
                .orderBy(qFollow.createdAt.desc())
                .offset((page - 1) * size)
                .limit(size)
                .fetch();

        Map<String, Object> result = new HashMap<>();
        result.put("followerList", followerList); // list
        result.put("totalPage", totalPage); // int

        return result;
    }

    public Map<String, Object> findMyFollowersNewzyList(int page, Long userId) {

        QNewzy qnewzy = QNewzy.newzy;
        QUser qUser = QUser.user;
        QFollow qFollow = QFollow.follow;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qnewzy.isDeleted.eq(false));  // 게시글이 삭제되지 않은 조건
        builder.and(qFollow.fromUser.userId.eq(userId));  // 내가 팔로우한 사용자의 조건 추가

        // 조인을 통해 내가 팔로우한 사용자의 게시글만 필터링
        Long totalCount = queryFactory
                .select(qnewzy.count())
                .from(qnewzy)
                .join(qnewzy.user, qUser)  // Newzy의 user와 Join
                .join(qFollow).on(qFollow.toUser.eq(qUser))  // Follow 테이블의 toUser와 Join
                .where(builder)  // 동적 조건
                .fetchOne();

        int totalPage = (int) ((totalCount + size - 1) / size);

        List<NewzyListGetResponseDTO> myFollowersNewzyList = queryFactory
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
                .join(qnewzy.user, qUser)  // Newzy의 user와 Join
                .join(qFollow).on(qFollow.toUser.eq(qUser))  // Follow 테이블의 toUser와 Join
                .where(builder)  // 동적 조건 적용
                .orderBy(qnewzy.createdAt.desc())  // 최신순 정렬
                .offset((page - 1) * size)  // 페이지네이션 offset
                .limit(size)  // 페이지네이션 limit
                .fetch();

        Map<String, Object> result = new HashMap<>();
        result.put("myFollowersNewzyList", myFollowersNewzyList); // list
        result.put("totalPage", totalPage); // int

        return result;
    }

}
