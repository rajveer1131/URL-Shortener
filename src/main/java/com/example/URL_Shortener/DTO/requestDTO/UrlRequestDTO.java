package com.example.URL_Shortener.DTO.requestDTO;

import lombok.Data;

@Data
public class UrlRequestDTO {
    private Long userId;
    private String originalUrl;
}
    