package com.example.URL_Shortener.DTO.responseDTO;

import com.example.URL_Shortener.Models.Click;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private List<Click> clickDetails;
}
