package com.accenture.pregunton.exception;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GameOverExceptionTest {

  @Test
  public void constructor_ShouldCreateANewInstanceOfGameCodeFoundExceptionTest() {
    assertThat(new GameOverException("nickname")).isInstanceOf(GameOverException.class)
        .isNotNull();
  }
}
