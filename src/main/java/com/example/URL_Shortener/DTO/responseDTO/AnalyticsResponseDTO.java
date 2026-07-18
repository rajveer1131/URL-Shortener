package com.example.URL_Shortener.DTO.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponseDTO {

    private Long urlId;
    private String shortCode;
    private String originalUrl;
    private int totalClicks;
    private LocalDateTime expiresDate;
    private boolean isExpired;
    private List<ClickResponseDTO> clickDetails;
}
