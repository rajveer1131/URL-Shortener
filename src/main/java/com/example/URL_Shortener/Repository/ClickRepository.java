package com.example.URL_Shortener.Repository;

import com.example.URL_Shortener.Models.Click;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClickRepository extends JpaRepository<Click, Long> {
    List<Click> findByUrlId(Long urlId);

    @Query("select COUNT(c) from Click c where c.url.id = :urlId")
    int getClickCount(@Param("urlId") Long urlId);

    @Modifying
    @Transactional
    @Query(value = """
    DELETE c
    FROM click c
    JOIN url u ON c.url_id = u.id
    WHERE u.expires_date < :now
    """, nativeQuery = true)
    void deleteClicksByExpiredUrls(@Param("now") LocalDateTime now);
}
