package com.example.URL_Shortener.DTO.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ClickRequestDTO {

    @NotNull(message = "URL ID is mandatory")
    private Long urlId;

    @NotBlank(message = "IP address cannot be blank")
    // Validates both IPv4 and IPv6 structural formats
    @Pattern(
            regexp = "^([0-9]{1,3}\\.){3}[0-9]{1,3}$|^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$",
            message = "Please provide a valid IPv4 or IPv6 address"
    )
    private String ipAddress;

    @NotBlank(message = "User Agent cannot be blank")
    private String userAgent;

}
