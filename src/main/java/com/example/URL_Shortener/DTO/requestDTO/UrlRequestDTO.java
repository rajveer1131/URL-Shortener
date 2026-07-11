package com.example.URL_Shortener.DTO.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlRequestDTO {
    private Long userId;
    private String originalUrl;
}
    