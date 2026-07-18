package com.example.URL_Shortener.Controller;


import com.example.URL_Shortener.DTO.requestDTO.ClickRequestDTO;
import com.example.URL_Shortener.DTO.responseDTO.AnalyticsResponseDTO;
import com.example.URL_Shortener.DTO.responseDTO.ApiResponse;
import com.example.URL_Shortener.DTO.responseDTO.ClickResponseDTO;
import com.example.URL_Shortener.Models.Click;
import com.example.URL_Shortener.Models.Url;
import com.example.URL_Shortener.Service.ClickService;
import com.example.URL_Shortener.Service.UrlService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
public class ClickController {

    private static final Logger logger = LoggerFactory.getLogger(ClickController.class);
    private final ClickService clickService;
    private final UrlService urlService;

    public ClickController(ClickService clickService, UrlService urlService) {
        this.clickService = clickService;
        this.urlService = urlService;
    }


    @PostMapping("/api/clicks/record")
    public ResponseEntity<ApiResponse<?>> recordClick(@Valid @RequestBody ClickRequestDTO clickRequestDTO) {
        Url url = urlService.getUrlById(clickRequestDTO.getUrlId());
        clickService.recordClick(url, clickRequestDTO.getIpAddress(), clickRequestDTO.getUserAgent());
        return new ResponseEntity<>(ApiResponse.success(null, "Click Recorded Successfully"), HttpStatus.OK);

    }

    @GetMapping("/api/analytics/{urlId}")
    public ResponseEntity<ApiResponse<?>> getAnalytics(@PathVariable Long urlId) {
        Url url = urlService.getUrlByIdWithClicks(urlId);
        List<Click> clicks = url.getClicks();
        AnalyticsResponseDTO responseDTO = AnalyticsResponseDTO.builder()
                .urlId(url.getId())
                .shortCode(url.getShortCode())
                .originalUrl(url.getOriginalUrl())
                .totalClicks(clickService.getClickCount(urlId))
                .expiresDate(url.getExpiresDate())
                .isExpired(url.getExpiresDate().isBefore(LocalDateTime.now()))
                .clickDetails(clicks.stream().map(click -> ClickResponseDTO.builder()
                        .accessDate(click.getAccessDate())
                        .ipAddress(click.getIpAddress())
                        .userAgent(click.getUserAgent())
                        .build()).toList())
                .build();


        return new ResponseEntity<>(ApiResponse.success(responseDTO, "Successfully fetched Analytics"), HttpStatus.OK);

    }
}
