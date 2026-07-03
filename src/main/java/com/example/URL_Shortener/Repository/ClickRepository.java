package com.example.URL_Shortener.Repository;

import com.example.URL_Shortener.Models.Click;
import com.example.URL_Shortener.Models.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClickRepository extends JpaRepository<Click, Long> {
    List<Click> findByUrlId(Long urlId);
}
