package com.mor.backend.exeptions.advice;

import com.mor.backend.common.ErrorMessage;
import com.mor.backend.exeptions.TokenRefreshException;
import com.mor.backend.payload.response.ObjectResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;


@RestControllerAdvice
public class TokenRefreshExceptionHandler {

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ObjectResponse handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        return new ObjectResponse("403", "Forbidden", new ErrorMessage(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)));
    }
}
