package com.example.URL_Shortener.DTO.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlRequestDTO {

    @NotNull(message = "User Id can not be blank")
    private Long userId;

    @NotBlank(message = "url can not be blank")
    @URL(message = "Must be a valid URL starting with http:// or https://")
    private String originalUrl;
}
    