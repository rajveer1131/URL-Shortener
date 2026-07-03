package com.example.URL_Shortener.Repository;

import com.example.URL_Shortener.Models.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortCode(String shortCode);

    List<Url> findByUserId(Long id);
}
