package com.example.oreo.exception;

import java.time.LocalDateTime;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorDetailsDto> handleBaseApiExceptions(BaseException ex, WebRequest request) {
        ResponseStatus responseStatusAnnotation = AnnotatedElementUtils.findMergedAnnotation(
            ex.getClass(), ResponseStatus.class);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; 
        String errorName = status.getReasonPhrase().toUpperCase().replace(" ", "_");

        if (responseStatusAnnotation != null) {
            status = responseStatusAnnotation.value();
            errorName = !responseStatusAnnotation.reason().isEmpty() ? 
                        responseStatusAnnotation.reason() : 
                        status.getReasonPhrase().toUpperCase().replace(" ", "_");
        }

        ErrorDetailsDto errorDetails = new ErrorDetailsDto(
            errorName,
            ex.getMessage(),
            LocalDateTime.now(),
            request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorDetails, status);
    }
}
