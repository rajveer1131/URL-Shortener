package com.example.URL_Shortener.Controller;

import com.example.URL_Shortener.DTO.requestDTO.UrlRequestDTO;
import com.example.URL_Shortener.DTO.responseDTO.ApiResponse;
import com.example.URL_Shortener.DTO.responseDTO.UrlResponseDTO;
import com.example.URL_Shortener.Models.Url;
import com.example.URL_Shortener.Models.User;
import com.example.URL_Shortener.Service.ClickService;
import com.example.URL_Shortener.Service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequestMapping("/api/urls")
public class UrlController {

    private static final Logger logger = LoggerFactory.getLogger(UrlController.class);
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
    public ResponseEntity<ApiResponse<?>> shortenUrl(@Valid @RequestBody UrlRequestDTO urlRequestDTO) {
        User user = new User();
        user.setId(urlRequestDTO.getUserId());
        Url url = urlService.shortenUrl(user, urlRequestDTO.getOriginalUrl(),urlRequestDTO.getShortCode(), urlRequestDTO.getExpiresDate());
        UrlResponseDTO urlResponseDTO = UrlResponseDTO.builder()
                .id(url.getId())
                .originalUrl(url.getOriginalUrl())
                .shortCode(url.getShortCode())
                .totalClicks(clickService.getClickCount(url.getId()))
                .createdDate(url.getCreatedDate())
                .expiresDate(url.getExpiresDate())
                .build();
        return new ResponseEntity<>(ApiResponse.success(urlResponseDTO, "URL shortened successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/{shortCode}")
    public RedirectView redirectToOriginalUrl(@PathVariable("shortCode") String shortCode, HttpServletRequest request) {

        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        String originalUrl = urlService.getOriginalUrlAndRecordClick(shortCode, ipAddress, userAgent);
        RedirectView redirectView = new RedirectView(originalUrl);
        redirectView.setStatusCode(HttpStatus.FOUND);
        return redirectView;

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
        urlService.deleteUrlById(urlId);
        return new ResponseEntity<>(ApiResponse.success(null, "URL deleted successfully"), HttpStatus.OK);

    }
}
