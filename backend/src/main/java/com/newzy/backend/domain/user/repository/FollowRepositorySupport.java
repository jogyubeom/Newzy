package com.newzy.backend.domain.user.repository;

import com.newzy.backend.domain.image.entity.QImage;
import com.newzy.backend.domain.newzy.dto.response.NewzyListGetResponseDTO;
import com.newzy.backend.domain.newzy.entity.QNewzy;
import com.newzy.backend.domain.user.dto.response.FollowListGetResponseDTO;
import com.newzy.backend.domain.user.entity.QFollow;
import com.newzy.backend.domain.user.entity.QUser;
import com.newzy.backend.domain.user.entity.User;
import com.newzy.backend.global.exception.EntityNotFoundException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Repository
public class FollowRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    private final int size = 20;

    @Autowired
    public FollowRepositorySupport(JPAQueryFactory queryFactory) {
        super(FollowRepository.class);
        this.queryFactory = queryFactory;
    }

//
//    public Map<String, Object> findFollowingList(Long userId, int page, String nickname) {
//        final UserRepository userRepository;
//        QFollow qFollow = QFollow.follow;
//        QUser qUserFrom = QUser.user;  // fromUser 엔티티
//        QUser qUserTo = QUser.user;  // toUser 엔티티
//
//        BooleanBuilder builder = new BooleanBuilder();
//        builder.and(qFollow.fromUser.nickname.eq(nickname.trim()));
//
//        Long totalCount = queryFactory
//                .select(qFollow.count())
//                .from(qFollow)
//                .leftJoin(qFollow.fromUser, qUserFrom)  // fromUser와 User 조인
//                .leftJoin(qFollow.toUser, qUserTo)  // toUser와 User 조인
//                .where(builder)  // 필터 조건 적용
//                .fetchCount();
//
//        int totalPage = (int) ((totalCount + size - 1) / size);
//
//        List<FollowListGetResponseDTO> followingList = queryFactory
//                .select(Projections.constructor(FollowListGetResponseDTO.class,
//                        qFollow.followId,
//                        qFollow.fromUser.nickname,
//                        qFollow.toUser.nickname
//                ))
//                .from(qFollow)
//                .leftJoin(qFollow.fromUser, qUserFrom)  // fromUser와 User 조인
//                .leftJoin(qFollow.toUser, qUserTo)  // toUser와 User 조인
//                .where(builder)
//                .orderBy(qFollow.createdAt.desc())
//                .offset((page - 1) * size)
//                .limit(size)
//                .fetch();
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("followingList", followingList); // list
//        result.put("totalPage", totalPage); // int
//
//        return result;
//
//    }
//
//    public Map<String, Object> findFollowerList(int page, String nickname) {
//        QFollow qFollow = QFollow.follow;
//        QUser qUserFrom = QUser.user;  // fromUser 엔티티
//        QUser qUserTo = QUser.user;  // toUser 엔티티
//
//        BooleanBuilder builder = new BooleanBuilder();
//        builder.and(qFollow.toUser.nickname.eq(nickname.trim()));
//
//        Long totalCount = queryFactory
//                .select(qFollow.count())
//                .from(qFollow)
//                .leftJoin(qFollow.fromUser, qUserFrom)  // fromUser와 User 조인
//                .leftJoin(qFollow.toUser, qUserTo)  // toUser와 User 조인
//                .where(builder)  // 필터 조건 적용
//                .fetchCount();
//
//        int totalPage = (int) ((totalCount + size - 1) / size);
//
//        List<FollowListGetResponseDTO> followerList = queryFactory
//                .select(Projections.constructor(FollowListGetResponseDTO.class,
//                        qFollow.followId,
//                        qFollow.fromUser.nickname,
//                        qFollow.toUser.nickname
//                ))
//                .from(qFollow)
//                .leftJoin(qFollow.fromUser, qUserFrom)  // fromUser와 User 조인
//                .leftJoin(qFollow.toUser, qUserTo)  // toUser와 User 조인
//                .where(builder)
//                .orderBy(qFollow.createdAt.desc())
//                .offset((page - 1) * size)
//                .limit(size)
//                .fetch();
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("followerList", followerList); // list
//        result.put("totalPage", totalPage); // int
//
//        return result;
//    }

}
