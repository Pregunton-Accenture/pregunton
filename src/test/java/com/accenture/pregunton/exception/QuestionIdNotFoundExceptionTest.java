package com.accenture.pregunton.exception;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionIdNotFoundExceptionTest {

  @Test
  public void constructor_ShouldCreateANewInstanceOfQuestionIdNotFoundException() {
    assertThat(new QuestionIdNotFoundException(1L)).isInstanceOf(QuestionIdNotFoundException.class)
        .isNotNull();
  }
}
