package com.example.URL_Shortener.Utils;

import com.example.URL_Shortener.Repository.ClickRepository;
import com.example.URL_Shortener.Repository.UrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UrlCleanupTask {

    private static final Logger logger = LoggerFactory.getLogger(UrlCleanupTask.class);
    private final UrlRepository urlRepository;
    private final ClickRepository clickRepository;

    public UrlCleanupTask(UrlRepository urlRepository,ClickRepository clickRepository) {
        this.urlRepository = urlRepository;
        this.clickRepository = clickRepository;
    }

    @Scheduled(cron = "0 0 2 * * *") // This is interval/time format -> sec min hour day month year
    public void ExpiredUrls() {
        logger.info("Starting cleanup of expired URLs");
        clickRepository.deleteClicksByExpiredUrls(LocalDateTime.now());
        urlRepository.deleteExpiredUrls(LocalDateTime.now());
        logger.info("Cleanup completed");
    }

}
