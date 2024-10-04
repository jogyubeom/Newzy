package com.newzy.backend.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class FollowRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;
    private final int size = 10;

    public FollowRepositorySupport(JPAQueryFactory queryFactory) {
        super(FollowRepository.class);
        this.queryFactory = queryFactory;
    }



}
