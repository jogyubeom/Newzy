package com.newzy.backend.global.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LogConfig implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(LogConfig.class);

    private String uniqueId;

    @PostConstruct
    public void init() {
        uniqueId = UUID.randomUUID().toString();
        System.setProperty("LOG_FILE_NAME", uniqueId + "-backend.log");
        logger.info("Unique log file name set to: {}", System.getProperty("LOG_FILE_NAME"));
    }

    @Override
    public void run(String... args) {
        // 추가 초기화 작업
        logger.info("로그 파일이 생성되었습니다: {}", System.getProperty("LOG_FILE_NAME"));
    }
}
