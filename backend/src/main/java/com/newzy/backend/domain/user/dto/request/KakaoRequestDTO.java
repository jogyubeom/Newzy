package com.newzy.backend.domain.user.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KakaoRequestDTO {
    private String email;
    private String nickname;
    private String type;
}
