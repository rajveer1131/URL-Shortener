package com.example.URL_Shortener.Service;

import com.example.URL_Shortener.Exception.ResourceNotFoundException;
import com.example.URL_Shortener.Models.Url;
import com.example.URL_Shortener.Models.User;
import com.example.URL_Shortener.Repository.UrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class UrlService {

    private static final Logger logger = LoggerFactory.getLogger(UrlService.class);

    private final UrlRepository urlRepository;
    private final ClickService clickService;

    public UrlService(UrlRepository urlRepository, ClickService clickService) {
        this.urlRepository = urlRepository;
        this.clickService = clickService;
    }

    public Url shortenUrl(User user, String originalUrl, LocalDateTime expiresDate) {
        logger.info("Receive expiration Date: {}", expiresDate);
        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortCode(generateShortCode());
        url.setCreatedDate(LocalDateTime.now());
        url.setExpiresDate(expiresDate);
        url.setUser(user);

        return urlRepository.save(url);


    }

    public String getOriginalUrl(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("ShortCode not found")).getOriginalUrl();
    }

    public String getOriginalUrlAndRecordClick(String shortCode, String ipAddress, String userAgent) {

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("ShortCode not found"));
        if (url.getExpiresDate() != null && url.getExpiresDate().isBefore(LocalDateTime.now())) {
            throw new ResourceNotFoundException("This URL has expired");
        }
        clickService.recordClick(url, ipAddress, userAgent);

        return url.getOriginalUrl();
    }

    public List<Url> getUserUrls(Long userId) {
        return urlRepository.findByUserId(userId);
    }

    public Url getUrlById(Long id) {
        return urlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Url does not exist"));
    }

    public Url getUrlByIdWithClicks(Long id) {
        return urlRepository.findByUrlIdWithClicks(id)
                .orElseThrow(() -> new ResourceNotFoundException("Url does not exist"));
    }

    public void deleteUrlById(Long id) {
        if (!urlRepository.existsById(id)) {
            throw new ResourceNotFoundException("URL does not exist");
        }
        urlRepository.deleteById(id);
    }

    public String generateShortCode() {
        String code;
        do {
            code = generateRandomShortCode();
        } while (urlRepository.findByShortCode(code).isPresent());

        return code;
    }

    private String generateRandomShortCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder result = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }
        return result.toString();
    }
}
