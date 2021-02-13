package com.accenture.pregunton.exception;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LastQuestionNotAnswerExceptionTest {

  @Test
  public void constructor_ShouldCreateANewInstanceOfGameCodeFoundException() {
    assertThat(new LastQuestionNotAnswerException("nickname")).isInstanceOf(LastQuestionNotAnswerException.class)
        .isNotNull();
  }
}
