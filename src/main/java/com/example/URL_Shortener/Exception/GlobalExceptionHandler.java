package com.example.URL_Shortener.Exception;

import com.example.URL_Shortener.DTO.responseDTO.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(ResourceNotFoundException e){
        return new ResponseEntity<>(ApiResponse.error(e.getMessage()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ShortenUrlAlreadyExists.class)
    public ResponseEntity<ApiResponse<?>> handleShortCodeAlreadyExists(ShortenUrlAlreadyExists e){
        return new ResponseEntity<>(ApiResponse.error(e.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public  ResponseEntity<ApiResponse<?>> handleUserAlreadyExists(UserAlreadyExistsException e){
        return new ResponseEntity<>(ApiResponse.error(e.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgument(IllegalArgumentException e) {
        return new ResponseEntity<>(ApiResponse.error(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<?>> handleNullPointer(NullPointerException e) {
        return new ResponseEntity<>(ApiResponse.error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)  // Catches ANY exception not matched above
    public ResponseEntity<ApiResponse<?>> handleGeneral(Exception e) {
        return new ResponseEntity<>(ApiResponse.error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}