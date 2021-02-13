package com.accenture.pregunton.exception;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GameCodeIdFoundExceptionTest {

  @Test
  public void constructor_ShouldCreateANewInstanceOfGameCodeFoundException() {
    assertThat(new GameIdNotFoundException(1L)).isInstanceOf(GameIdNotFoundException.class)
        .isNotNull();
  }
}
