package com.example.URL_Shortener.Service;

import com.example.URL_Shortener.Models.Url;
import com.example.URL_Shortener.Models.User;
import com.example.URL_Shortener.Repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public String shortenUrl(User user, String originalUrl){
        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortCode(generateShortCode());
        url.setCreatedDate(LocalDateTime.now());
        url.setUser(user);

        return urlRepository.save(url).getShortCode();


    }

    public String getOriginalUrl(String shortCode){
        return urlRepository.findByShortCode(shortCode)
                .orElseThrow(()-> new IllegalArgumentException("ShortCode not found"))
                .getOriginalUrl();
    }

    public List<Url> getUserUrls(User user){
        return urlRepository.findByUserId(user.getId());
    }

    public Url getUrlById(Long id) throws IllegalArgumentException{
        return urlRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Url does not exist"));
    }

    public void deleteUrlById(Long id) {
        if(!urlRepository.existsById(id)) {
            throw new IllegalArgumentException("URL does not exist");
        }
        urlRepository.deleteById(id);
    }

    public String generateShortCode(){
       String code;
       do{
           code=generateRandomShortCode();
       }while(urlRepository.findByShortCode(code).isPresent());

       return code;
    }

    private String generateRandomShortCode(){
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
