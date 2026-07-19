package com.example.URL_Shortener.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String originalUrl;
    @Column(nullable = false, unique = true)
    private String shortCode;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private User user;
    @OneToMany(mappedBy = "url",cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Click> clicks;
    @CreationTimestamp
    private LocalDateTime createdDate;
    private LocalDateTime expiresDate;
}
