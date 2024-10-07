package com.newzy.backend.domain.user.entity;

import com.newzy.backend.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "follow")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "follow")
@ToString
@Builder
public class Follow extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id", unique = true, nullable = false)
    private Long followId;

    @ManyToOne
    @JoinColumn(name = "to_id")
    private User toUser;

    @ManyToOne
    @JoinColumn(name = "from_id")
    private User fromUser;

}
