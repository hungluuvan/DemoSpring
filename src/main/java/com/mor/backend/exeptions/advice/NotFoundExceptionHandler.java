package com.mor.backend.exeptions.advice;

import com.mor.backend.exeptions.NotFoundException;
import com.mor.backend.payload.response.ObjectResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;


@RestControllerAdvice
public class NotFoundExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ObjectResponse handleNotFoundException(NotFoundException ex, WebRequest request) {
        return new ObjectResponse("400", ex.getMessage(), "");
    }
}