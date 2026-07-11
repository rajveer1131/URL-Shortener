package com.example.URL_Shortener.DTO.requestDTO;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String username;
    private String email;
    private String password;
}