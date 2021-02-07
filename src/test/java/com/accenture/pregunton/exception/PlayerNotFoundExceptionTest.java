package com.accenture.pregunton.exception;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerNotFoundExceptionTest {

  @Test
  public void constructor_ShouldCreateANewInstanceOfGameCodeFoundExceptionTest() {
    assertThat(new PlayerNotFoundException(1L)).isInstanceOf(PlayerNotFoundException.class)
        .isNotNull();
  }
}
