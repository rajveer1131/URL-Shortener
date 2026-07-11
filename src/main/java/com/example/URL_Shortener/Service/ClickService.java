package com.example.URL_Shortener.Service;

import com.example.URL_Shortener.Models.Click;
import com.example.URL_Shortener.Models.Url;
import com.example.URL_Shortener.Repository.ClickRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClickService {

    private final ClickRepository clickRepository;

    public ClickService(ClickRepository clickRepository) {
        this.clickRepository = clickRepository;
    }

    public void recordClick(Url url, String ipAddress, String userAgent) {
        Click click = new Click();
        click.setUrl(url);
        click.setIpAddress(ipAddress);
        click.setUserAgent(userAgent);
        click.setAccessDate(LocalDateTime.now());
        clickRepository.save(click);
    }

    public List<Click> getClicksByUrl(Long urlId) {
        return clickRepository.findByUrlId(urlId);
    }

    public int getClickCount(Long urlId) {
        return clickRepository.findByUrlId(urlId).size();
    }
}
