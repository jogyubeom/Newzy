package com.newzy.backend.domain.user.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class FollowListGetResponseDTO {

    private Long followId;
    private String toUserNickname;
    private String fromUserNickname;

}
