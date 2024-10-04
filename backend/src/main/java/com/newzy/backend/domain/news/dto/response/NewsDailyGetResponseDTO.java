package com.newzy.backend.domain.news.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class NewsDailyGetResponseDTO {
    private Long newsId;
    private String link;
    private String summary;
    private String thumbnail;

    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String answer;
}
