package com.newzy.scheudling.global.config;

import com.newzy.scheudling.domain.card.service.NewsCardService;
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
}
