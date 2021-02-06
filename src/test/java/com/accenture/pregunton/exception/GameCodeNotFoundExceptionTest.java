package com.accenture.pregunton.exception;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GameCodeNotFoundExceptionTest {

  @Test
  public void constructor_ShouldCreateANewInstanceOfGameCodeFoundExceptionTest() {
    assertThat(new GameCodeNotFoundException("CODE01")).isInstanceOf(GameCodeNotFoundException.class)
        .isNotNull();
  }
}
