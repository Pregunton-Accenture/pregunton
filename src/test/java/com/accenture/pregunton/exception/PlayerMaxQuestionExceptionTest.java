package com.accenture.pregunton.exception;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerMaxQuestionExceptionTest {

  @Test
  public void constructor_ShouldCreateANewInstanceOfGameCodeFoundException() {
    assertThat(new PlayerMaxQuestionException("nickname")).isInstanceOf(PlayerMaxQuestionException.class)
        .isNotNull();
  }
}
