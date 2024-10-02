package com.newzy.backend.domain.news.dto.response;

import com.newzy.backend.domain.news.entity.News;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class NewsRecommendGetResponseDTO {
    private Long newsId;
    private String link;
    private String summary;
    private String thumbnail;
}
