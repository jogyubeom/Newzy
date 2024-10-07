package com.newzy.backend.domain.user.repository;

import com.newzy.backend.domain.user.entity.Cluster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClusterRepository extends JpaRepository<Cluster, Long> {

}
