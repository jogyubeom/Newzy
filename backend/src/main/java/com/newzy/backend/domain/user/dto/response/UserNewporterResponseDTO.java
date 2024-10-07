package com.newzy.backend.domain.user.dto.response;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserNewporterResponseDTO {
    private String nickname;
    private String profile;
    private Long count;
}
