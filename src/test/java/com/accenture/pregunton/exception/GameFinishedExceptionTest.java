package com.accenture.pregunton.exception;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GameFinishedExceptionTest {

  @Test
  public void constructor_ShouldCreateANewInstanceOfGameFinishedException() {
    assertThat(new GameFinishedException()).isInstanceOf(GameFinishedException.class)
        .isNotNull();
  }
}
