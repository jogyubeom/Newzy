package com.newzy.backend.domain.newzy.repository;

import com.newzy.backend.domain.newzy.entity.Newzy;
import com.newzy.backend.domain.newzy.entity.NewzyLike;
import com.newzy.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewzyLikeRepository extends JpaRepository<NewzyLike, Long> {

    boolean existsByUserAndNewzy(User user, Newzy newzy);

    void deleteByUserAndNewzy(User user, Newzy newzy);
}
