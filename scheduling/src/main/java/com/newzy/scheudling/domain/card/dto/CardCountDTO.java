package com.newzy.scheudling.domain.card.dto;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardCountDTO {
    private Long userId;
    private Long count;
}
