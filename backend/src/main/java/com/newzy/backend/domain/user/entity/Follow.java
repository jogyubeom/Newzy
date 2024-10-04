package com.newzy.backend.domain.user.entity;

import com.newzy.backend.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity(name = "follow")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "follow")
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

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = Boolean.FALSE;


}
