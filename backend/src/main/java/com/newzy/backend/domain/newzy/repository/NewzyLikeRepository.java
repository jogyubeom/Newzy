package com.newzy.backend.domain.newzy.repository;

import com.newzy.backend.domain.newzy.entity.NewzyLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewzyLikeRepository extends JpaRepository<NewzyLike, Long> {

}
