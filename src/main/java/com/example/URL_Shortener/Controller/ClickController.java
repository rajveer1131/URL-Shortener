package com.example.URL_Shortener.Controller;


import com.example.URL_Shortener.DTO.requestDTO.ClickRequestDTO;
import com.example.URL_Shortener.DTO.responseDTO.AnalyticsResponseDTO;
import com.example.URL_Shortener.Models.Click;
import com.example.URL_Shortener.Models.Url;
import com.example.URL_Shortener.Service.ClickService;
import com.example.URL_Shortener.Service.UrlService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClickController {

    private final ClickService clickService;
    private final UrlService urlService;

    public ClickController(ClickService clickService, UrlService urlService) {
        this.clickService = clickService;
        this.urlService = urlService;
    }

    @Data
    static class AnalyticsResponse {

    }


    @PostMapping("/api/clicks/record")
    public ResponseEntity<String> recordClick(@RequestBody ClickRequestDTO clickRequestDTO) {


        clickService.recordClick(clickRequestDTO.getUrlId(), clickRequestDTO.getIpAddress(), clickRequestDTO.getUserAgent());
        return new ResponseEntity<>("Click Recorded Successfully", HttpStatus.OK);

    }

    @GetMapping("/api/analytics/{urlId}")
    public ResponseEntity<?> getAnalytics(@PathVariable Long urlId) {

        Url url = urlService.getUrlById(urlId);  // Get URL details
        List<Click> clicks = clickService.getClicksByUrl(urlId);

        AnalyticsResponseDTO responseDTO = new AnalyticsResponseDTO();
        responseDTO.setUrlId(url.getId());
        responseDTO.setShortCode(url.getShortCode());
        responseDTO.setOriginalUrl(url.getOriginalUrl());
        responseDTO.setTotalClicks(clicks.size());
        responseDTO.setClickDetails(clicks);

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);

    }
}
