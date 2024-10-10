package com.newzy.backend.domain.vocaTest.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "test_word")
@ToString
public class TestWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id", unique = true, nullable = false)
    private Long wordId;

    @Column(name = "word")
    private String word;

    @Column(name = "category")
    private int category;

}
