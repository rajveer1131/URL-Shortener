package com.example.URL_Shortener.Repository;

import com.example.URL_Shortener.Models.Url;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortCode(String shortCode);

    List<Url> findByUserId(Long id);

    @Query("SELECT u FROM Url u LEFT JOIN FETCH u.clicks WHERE u.id = :urlId")
    Optional<Url> findByUrlIdWithClicks(@Param("urlId") Long urlId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Url u WHERE u.expiresDate < :now AND u.expiresDate IS NOT NULL")
    void deleteExpiredUrls(@Param("now") LocalDateTime now);

}
