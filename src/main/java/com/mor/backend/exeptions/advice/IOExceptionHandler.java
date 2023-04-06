package com.mor.backend.exeptions.advice;

import com.mor.backend.payload.response.ObjectResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice

public class IOExceptionHandler {
    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ObjectResponse> handleIOException(IOException ex) {
        return new ResponseEntity<>(new ObjectResponse("500", ex.getMessage(), ""), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

