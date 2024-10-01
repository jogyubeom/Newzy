package com.newzy.backend.domain.newzy.repository;

import com.newzy.backend.domain.newzy.entity.NewzyBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewzyBookmarkRepository extends JpaRepository<NewzyBookmark, Long> {


}
