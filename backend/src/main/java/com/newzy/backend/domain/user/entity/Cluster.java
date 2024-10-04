package com.newzy.backend.domain.user.entity;

import com.newzy.backend.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "cluster")
@DynamicUpdate
@ToString
public class Cluster extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cluster_id", unique = true, nullable = false)
    private int clusterId;
    @Column(name = "cluster_name")
    private String clusterName;
    @Column(name = "age_group")
    private String ageGroup;
    @Column(name = "interest_category")
    private String interestCategory;
    @Column(name = "page_stay_time")
    private int pageStayTime;
}
