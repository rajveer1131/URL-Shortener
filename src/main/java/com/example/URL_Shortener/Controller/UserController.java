package com.example.URL_Shortener.Controller;

import com.example.URL_Shortener.DTO.requestDTO.UserRequestDTO;
import com.example.URL_Shortener.DTO.responseDTO.ApiResponse;
import com.example.URL_Shortener.DTO.responseDTO.UserResponseDTO;
import com.example.URL_Shortener.Models.User;
import com.example.URL_Shortener.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> registerUser(@RequestBody UserRequestDTO request) {
        try {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            User saved = userService.createUser(user);

            UserResponseDTO response = UserResponseDTO.builder()
                    .id(saved.getId())
                    .username(saved.getUsername())
                    .email(saved.getEmail())
                    .createdAt(saved.getCreatedAt())
                    .build();

            return new ResponseEntity<>(ApiResponse.success(response, "User registered successfully"), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(ApiResponse.error(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> getUserById(@PathVariable("userId") Long userId) {
        try {
            User user = userService.getUserById(userId);

            UserResponseDTO response = UserResponseDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .createdAt(user.getCreatedAt())
                    .build();

            return new ResponseEntity<>(ApiResponse.success(response, "User retrieved successfully"), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(ApiResponse.error(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }


}
