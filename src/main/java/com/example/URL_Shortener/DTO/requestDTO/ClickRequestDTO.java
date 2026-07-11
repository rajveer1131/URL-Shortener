package com.example.URL_Shortener.DTO.requestDTO;

import lombok.Data;

@Data
public class ClickRequestDTO {



        private Long urlId;
        private String ipAddress;
        private String userAgent;


}
