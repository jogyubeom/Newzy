package com.newzy.scheudling.global.config;

import com.newzy.scheudling.domain.card.service.NewsCardService;
import com.newzy.scheudling.domain.news.service.NewsService;
import com.newzy.scheudling.domain.newzy.service.NewzyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
@EnableRetry
public class SchedulerConfig {
    private final NewsCardService newsCardService;
    private final NewsService newsService;
    private final NewzyService newzyService;

    @Scheduled(cron = "0 0 7 ? * MON")
//@Scheduled(cron = "0 4 3 * * ?")
    public void scheduleNewsCard() {
        try {
            log.info("카드왕 계산 ㄱㄱ");
            newsCardService.calculateBestCardCollector();
        } catch (Exception e) {
            log.error("카드왕 계산 중 에러 발생", e);
        }
    }

    @Scheduled(cron = "0 0 7 ? * MON")
//@Scheduled(cron = "0 4 3 * * ?")
    public void scheduleNewporter() {
        try {
            log.info("뉴포터 계산 ㄱㄱ");
            newzyService.calculateNewporter();
        } catch (Exception e) {
            log.error("뉴포터 계산 중 에러 발생", e);
        }
    }

    // 매일 00시에 진행
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleNewsRanking() {
        try {
            log.info("뉴스 조회수 DB 반영");
            newsService.processNewsRanking();
        } catch (Exception e) {
            log.error("뉴스 조회수 DB 반영 중 에러 발생", e);
        }
    }

    // 매일 00시에 진행
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleNewzyRanking() {
        try {
            log.info("뉴지 조회수 DB 반영");
            newzyService.processNewzyRanking();
        } catch (Exception e) {
            log.error("뉴지 조회수 DB 반영 중 에러 발생", e);
        }
    }
}
