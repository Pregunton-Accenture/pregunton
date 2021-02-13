package com.accenture.pregunton.exception;

public class QuestionIdNotFoundException extends RuntimeException {

  public QuestionIdNotFoundException(Long id) {
    super("Question with ID " + id + " not found.");
  }
}
