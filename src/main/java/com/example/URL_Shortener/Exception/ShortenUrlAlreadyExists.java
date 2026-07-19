package com.example.URL_Shortener.Exception;

public class ShortenUrlAlreadyExists extends RuntimeException{
    public ShortenUrlAlreadyExists(String message){
        super(message);
    }
}
