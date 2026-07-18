package com.example.URL_Shortener.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data
public class Click {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "url_id")
    @JsonIgnore
    @ToString.Exclude
    private Url url;
    private LocalDateTime accessDate;
    private String ipAddress;
    private String userAgent;

}
