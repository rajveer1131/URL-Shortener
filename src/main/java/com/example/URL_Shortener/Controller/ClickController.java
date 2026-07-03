package com.example.URL_Shortener.Controller;


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

    public ClickController(ClickService clickService,UrlService urlService){
        this.clickService=clickService;
        this.urlService=urlService;
    }

    @Data
    static class ClickRequestBody{
        private  Long urlId;
        private String ipAddress;
        private String userAgent;


    }

    @Data
    static class AnalyticsResponse {
        private Long urlId;
        private String shortCode;
        private String originalUrl;
        private int totalClicks;
        private List<Click> clickDetails;
    }


    @PostMapping("/api/clicks/record")
    public ResponseEntity<String> recordClick(@RequestBody ClickRequestBody clickRequestBody){

        try{
            clickService.recordClick(clickRequestBody.getUrlId(),clickRequestBody.getIpAddress(),clickRequestBody.getUserAgent());
            return new ResponseEntity<>("Click Recorded Successfully" , HttpStatus.OK);
        }catch (IllegalArgumentException e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/analytics/{urlId}")
    public ResponseEntity<?> getAnalytics(@PathVariable Long urlId) {
        try {
            Url url = urlService.getUrlById(urlId);  // Get URL details
            List<Click> clicks = clickService.getClicksByUrl(urlId);

            AnalyticsResponse response = new AnalyticsResponse();
            response.setUrlId(url.getId());
            response.setShortCode(url.getShortCode());
            response.setOriginalUrl(url.getOriginalUrl());
            response.setTotalClicks(clicks.size());
            response.setClickDetails(clicks);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
