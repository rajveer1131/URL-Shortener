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
    private final UrlService urlService;

    public ClickService(ClickRepository clickRepository,UrlService urlService) {
        this.clickRepository = clickRepository;
        this.urlService=urlService;
    }

    public void recordClick(Long urlId,String ipAddress , String userAgent){
        Url url = urlService.getUrlById(urlId);
        Click click = new Click();
        click.setUrl(url);
        click.setIpAddress(ipAddress);
        click.setUserAgent(userAgent);
        click.setAccessDate(LocalDateTime.now());
        clickRepository.save(click);
    }

    public List<Click> getClicksByUrl(Long urlId){
        return clickRepository.findByUrlId(urlId);
    }

    public int getClickCount(Long urlId){
        return clickRepository.findByUrlId(urlId).size();
    }
}
