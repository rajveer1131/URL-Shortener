package com.example.URL_Shortener.DTO.responseDTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class UserResponseDTO {

    private Long userId;
    private String username;
    private String email;
    private LocalDateTime createdAt;

}
