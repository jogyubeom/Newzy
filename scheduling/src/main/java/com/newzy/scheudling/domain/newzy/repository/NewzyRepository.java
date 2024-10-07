package com.newzy.scheudling.domain.newzy.repository;

import com.newzy.scheudling.domain.newzy.entity.Newzy;
import com.newzy.scheudling.global.exception.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewzyRepository extends JpaRepository<Newzy, Long> {


}
