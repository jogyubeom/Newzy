package com.newzy.backend.domain.user.repository;

import com.newzy.backend.domain.user.entity.Follow;
import com.newzy.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    Follow findByFromUserAndToUser(User fromUser, User toUser);

    List<Follow> findAllByToUser(User toUser);

    List<Follow> findAllByFromUser(User fromUser);

    List<Follow> findAllByToUser_UserId(Long userId);  // 수정된 메서드

    List<Follow> findAllByFromUser_UserId(Long userId);  // 수정된 메서드

    // a가 b를 팔로우하는지 확인하기
    boolean existsByFromUserAndToUser(User fromUser, User toUser);

    // userId가 팔로우한 사람 수 (fromUser가 해당 userId일 때)
    Long countByFromUserUserId(Long userId);

    // userId를 팔로우한 사람 수 (toUser가 해당 userId일 때)
    Long countByToUserUserId(Long userId);
}
