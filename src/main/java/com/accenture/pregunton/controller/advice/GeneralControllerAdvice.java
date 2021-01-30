package com.accenture.pregunton.controller.advice;

import com.accenture.pregunton.exception.CategoryNotFoundException;
import com.accenture.pregunton.exception.GameIdNotFoundException;
import com.accenture.pregunton.exception.GameOverException;
import com.accenture.pregunton.exception.PlayerNotFoundException;
import com.accenture.pregunton.util.SimpleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GeneralControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> unexpectedErrorHandler(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(SimpleResponse.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build());
    }

    @ExceptionHandler(GameIdNotFoundException.class)
    protected ResponseEntity<Object> gameNotFoundHandler(GameIdNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(SimpleResponse.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    protected ResponseEntity<Object> categoryNotFoundHandler(CategoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(SimpleResponse.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    protected ResponseEntity<Object> playerNotFoundHandler(PlayerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(SimpleResponse.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }

    @ExceptionHandler(GameOverException.class)
    protected ResponseEntity<Object> gameOverHandler(GameOverException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(SimpleResponse.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .build());
    }

}
