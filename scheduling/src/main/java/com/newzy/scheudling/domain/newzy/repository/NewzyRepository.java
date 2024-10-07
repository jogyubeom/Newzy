package com.newzy.scheudling.domain.newzy.repository;

import com.newzy.scheudling.domain.newzy.entity.Newzy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewzyRepository extends JpaRepository<Newzy, Long> {


}
