package com.example.URL_Shortener.DTO.responseDTO;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ClickResponseDTO {


    private LocalDateTime accessDate;
    private String ipAddress;
    private String userAgent;

}
