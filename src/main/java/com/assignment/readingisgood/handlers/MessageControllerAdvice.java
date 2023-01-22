package com.assignment.readingisgood.handlers;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class MessageControllerAdvice {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<ObjectError> errorMessage = ex.getBindingResult().getAllErrors();
        String message = errorMessage.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("\n"));
        System.out.println(message);
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<String> handleParseExceptions(
            HttpMessageNotReadableException httpMessageNotReadableException) {
        return ResponseEntity.badRequest().body(httpMessageNotReadableException.getMessage());
    }
}
