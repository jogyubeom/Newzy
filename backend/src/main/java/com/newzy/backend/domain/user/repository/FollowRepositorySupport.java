package com.newzy.backend.domain.user.repository;

import com.newzy.backend.domain.user.dto.response.FollowListGetResponseDTO;
import com.newzy.backend.domain.user.entity.QFollow;
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
    private final int size = 10;

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
}
