package com.accenture.pregunton.controller.advice;

import com.accenture.pojo.SimpleResponse;
import com.accenture.pojo.UnauthorizedResponseDto;
import com.accenture.pregunton.exception.CategoryNotFoundException;
import com.accenture.pregunton.exception.GameCodeNotFoundException;
import com.accenture.pregunton.exception.GameFinishedException;
import com.accenture.pregunton.exception.GameIdNotFoundException;
import com.accenture.pregunton.exception.GameOverException;
import com.accenture.pregunton.exception.LastQuestionNotAnswerException;
import com.accenture.pregunton.exception.PlayerNotFoundException;
import com.accenture.pregunton.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GeneralControllerAdvice extends ResponseEntityExceptionHandler {

  private ResponseEntity<SimpleResponse> buildSimpleResponse(String message, HttpStatus httpStatus) {
    return ResponseEntity.status(httpStatus)
        .body(SimpleResponse.builder()
            .message(message)
            .status(httpStatus.value())
            .build());
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<SimpleResponse> unexpectedErrorHandler(Exception ex) {
    return buildSimpleResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(value = {
      GameIdNotFoundException.class,
      GameCodeNotFoundException.class,
      CategoryNotFoundException.class,
      PlayerNotFoundException.class
  })
  protected ResponseEntity<SimpleResponse> notFoundExceptionHandler(Exception ex) {
    return buildSimpleResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = {
      GameOverException.class,
      LastQuestionNotAnswerException.class,
      GameFinishedException.class
  })
  protected ResponseEntity<SimpleResponse> badRequestHandler(Exception ex) {
    return buildSimpleResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UnauthorizedException.class)
  protected ResponseEntity<UnauthorizedResponseDto> unauthorizedExceptionHandler(UnauthorizedException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new UnauthorizedResponseDto(ex.getPath()));
  }
}
