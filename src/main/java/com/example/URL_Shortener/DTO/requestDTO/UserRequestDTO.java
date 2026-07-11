package com.example.URL_Shortener.DTO.requestDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDTO {

    @NotBlank(message = "username can not be blank")
    @Size(min = 3, max = 20, message = "username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "email can not be blank")
    @Email
    private String email;
    @NotBlank(message = "password can not be blank")
    @Size(min = 5, max = 20, message = "password must be between 5 and 20 characters")
    private String password;
}