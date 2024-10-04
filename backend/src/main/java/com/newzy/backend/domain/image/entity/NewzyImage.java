package com.newzy.backend.domain.image.entity;

import com.newzy.backend.domain.newzy.entity.Newzy;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "newzy_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NewzyImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newzyImageId;

    @ManyToOne
    @JoinColumn(name = "newzy_id", nullable = false)
    private Newzy newzy;

    @ManyToOne
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    @Column(name = "`order`", nullable = false)
    private int order; // 1부터 시작

    @Column(nullable = false)
    private boolean isThumbnail;
}
