package com.mor.backend.exeptions.advice;

import com.mor.backend.payload.response.ObjectResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class MethodArgumentNotValidExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ObjectResponse> handleValidationErrors(BindException ex) {
        List<Error> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(a -> new Error(a.getField(), a.getDefaultMessage())).collect(Collectors.toList());
        return new ResponseEntity<>(new ObjectResponse("400", "Bad Request", getErrorsMap(errors)), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<Error>> getErrorsMap(List<Error> errors) {
        Map<String, List<Error>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

    static class Error {

        private final String message;
        private final String fieldError;

        Error(String fieldError, String message) {
            this.fieldError = fieldError;
            this.message = message;
        }

        public String getFieldError() {
            return fieldError;
        }

        public String getMessage() {
            return message;
        }


    }
}
