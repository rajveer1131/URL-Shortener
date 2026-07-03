package com.example.URL_Shortener.Controller;

import com.example.URL_Shortener.Models.Url;
import com.example.URL_Shortener.Models.User;
import com.example.URL_Shortener.Repository.UrlRepository;
import com.example.URL_Shortener.Service.UrlService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService){
        this.urlService = urlService;
    }

    /*
    POST /api/urls/shorten - Create short URL
    GET /api/{shortCode} - Redirect
    GET /api/urls/user/{userId} - Get user's URLs
    DELETE /api/urls/{id} - Delete URL
     */

    @Data
    static class ShortenUrlRequest {
        private Long userId;
        private String originalUrl;
    }
    @PostMapping("/api/urls/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody ShortenUrlRequest shortenUrlRequest) {
        try {
            User user = new User();
            user.setId(shortenUrlRequest.getUserId());
            String shortCode = urlService.shortenUrl(user, shortenUrlRequest.getOriginalUrl());
            return new ResponseEntity<>(shortCode, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/{shortCode}")
    public ResponseEntity<?> getOriginalUrl(@PathVariable("shortCode") String shortCode) {
        try {
            String originalUrl = urlService.getOriginalUrl(shortCode);
            return new ResponseEntity<>(originalUrl, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/api/urls/user/{userId}")
    public List<Url> getUserUrls(@PathVariable("userId") Long userId){
        User user = new User();
        user.setId(userId);
        return urlService.getUserUrls(user);
    }

    @DeleteMapping("/api/urls/{urlId}")
    public ResponseEntity<String> deleteUrl(@PathVariable("urlId") Long urlId){

        try{

        urlService.deleteUrlById(urlId);
            return new ResponseEntity<>("URL deleted successfully", HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }


    }
}
