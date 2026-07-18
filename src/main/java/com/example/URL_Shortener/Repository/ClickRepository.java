package com.example.URL_Shortener.Repository;

import com.example.URL_Shortener.Models.Click;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClickRepository extends JpaRepository<Click, Long> {
    List<Click> findByUrlId(Long urlId);

    @Query("select COUNT(c) from Click c where c.url.id = :urlId")
    int getClickCount(@Param("urlId") Long urlId);
}
