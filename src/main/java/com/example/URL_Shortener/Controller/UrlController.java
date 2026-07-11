package com.example.URL_Shortener.Controller;

import com.example.URL_Shortener.DTO.requestDTO.UrlRequestDTO;
import com.example.URL_Shortener.DTO.responseDTO.ApiResponse;
import com.example.URL_Shortener.DTO.responseDTO.UrlResponseDTO;
import com.example.URL_Shortener.Models.Url;
import com.example.URL_Shortener.Models.User;
import com.example.URL_Shortener.Service.ClickService;
import com.example.URL_Shortener.Service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/urls")
public class UrlController {

    private final UrlService urlService;
    private final ClickService clickService;

    public UrlController(UrlService urlService, ClickService clickService) {
        this.urlService = urlService;
        this.clickService = clickService;
    }


    /*
    POST /api/urls/shorten - Create short URL
    GET /api/{shortCode} - Redirect
    GET /api/urls/user/{userId} - Get user's URLs
    DELETE /api/urls/{id} - Delete URL
     */

    @PostMapping("/shorten")
    public ResponseEntity<ApiResponse<?>> shortenUrl(@RequestBody UrlRequestDTO urlRequestDTO) {
        try {
            User user = new User();
            user.setId(urlRequestDTO.getUserId());
            Url url = urlService.shortenUrl(user, urlRequestDTO.getOriginalUrl());
            UrlResponseDTO urlResponseDTO = UrlResponseDTO.builder()
                    .id(url.getId())
                    .originalUrl(url.getOriginalUrl())
                    .shortCode(url.getShortCode())
                    .createdDate(url.getCreatedDate())
                    .expiresDate(url.getExpiresDate())
                    .build();
            return new ResponseEntity<>(ApiResponse.success(urlResponseDTO, "URL shortened successfully"), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(ApiResponse.error(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<ApiResponse<?>> getOriginalUrl(@PathVariable("shortCode") String shortCode) {
        try {
            String originalUrl = urlService.getOriginalUrl(shortCode);

            return new ResponseEntity<>(ApiResponse.success(originalUrl, "Original URL Found"), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(ApiResponse.error(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<?>> getUserUrls(@PathVariable("userId") Long userId) {

        List<Url> urls = urlService.getUserUrls(userId);

        List<UrlResponseDTO> result = urls.stream()
                .map(url -> UrlResponseDTO.builder()
                        .id(url.getId())
                        .originalUrl(url.getOriginalUrl())
                        .shortCode(url.getShortCode())
                        .createdDate(url.getCreatedDate())
                        .expiresDate(url.getExpiresDate())
                        .build()).toList();
        return new ResponseEntity<>(ApiResponse.success(result, "All Url's for current User"), HttpStatus.OK);
    }

    @DeleteMapping("/{urlId}")
    public ResponseEntity<ApiResponse<?>> deleteUrl(@PathVariable("urlId") Long urlId) {

        try {

            urlService.deleteUrlById(urlId);
            return new ResponseEntity<>(ApiResponse.success(null, "URL deleted successfully"), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(ApiResponse.error(e.getMessage()), HttpStatus.NOT_FOUND);
        }


    }
}
