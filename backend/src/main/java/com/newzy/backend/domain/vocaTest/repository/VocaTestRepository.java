package com.newzy.backend.domain.vocaTest.repository;

import com.newzy.backend.domain.vocaTest.entity.TestWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VocaTestRepository extends JpaRepository<TestWord, Long> {
    List<TestWord> findByCategory(int category);
}
