package com.example.URL_Shortener.DTO.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlResponseDTO {
    private Long id;
    private String originalUrl;
    private String shortCode;
    private LocalDateTime createdDate;
    private LocalDateTime expiresDate;
}
