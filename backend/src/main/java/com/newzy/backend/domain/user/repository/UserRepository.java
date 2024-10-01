package com.newzy.backend.domain.user.repository;

import com.newzy.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    default User updateUserInfo(User updatedUser){
        User user = findById(updatedUser.getUserId()).orElseThrow(() -> new IllegalStateException("유저를 찾을 수 없습니다."));

        user.setNickname(updatedUser.getNickname());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());    // 비밀번호 추수 수정 필요
        user.setInfo(updatedUser.getInfo());
        user.setBirth(updatedUser.getBirth());
        user.setInfo(updatedUser.getInfo());

        return save(user);
    };

    default void deleteUserInfo(Long userId){
        User user = findById(userId).orElseThrow(() -> new IllegalStateException("해당 유저가 존재하지 않습니다. Id: " + userId));

        user.setIsDeleted(true);
        save(user);
    }

}
