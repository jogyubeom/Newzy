package com.newzy.backend.global.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@MappedSuperclass
public abstract class BaseTimeEntity {
    // created_at
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt ;

    // updated_at
    @Column(name = "update_at")
    private LocalDateTime updatedAt ;
}
