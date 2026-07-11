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

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        User savedUser = userService.createUser(user);

        UserResponseDTO result = UserResponseDTO.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .username(savedUser.getUsername())
                .createdAt(savedUser.getCreatedAt())
                .build();
        return new ResponseEntity<>(ApiResponse.success(result, "User got saved successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> getUserById(@PathVariable("userId") Long userId) {

        User user = userService.getUserById(userId);

        UserResponseDTO result = UserResponseDTO.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .createdAt(user.getCreatedAt())
                .build();
        return new ResponseEntity<>(ApiResponse.success(result, "User Found"), HttpStatus.OK);

    }




}
