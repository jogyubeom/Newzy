package com.newzy.backend.domain.user.entity;

import com.newzy.backend.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity(name = "follow")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "follow")
public class Follow extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id", unique = true, nullable = false)
    private Long followId;

    // TODO: 추후 다시 수정 (follow entity)
//    @ManyToOne
//    @JoinColumn(name = "to_id")
//    private User toId;
//
//    @ManyToOne
//    @JoinColumn(name = "from_id")
//    private User fromId;

    @Column
    private boolean isDeleted = Boolean.FALSE;


}
