package com.accenture.pregunton.exception;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UnauthorizedExceptionTest {

  @Test
  public void constructor_ShouldCreateANewInstanceOfUnauthorizedException() {
    assertThat(new UnauthorizedException("/path")).isInstanceOf(UnauthorizedException.class)
        .isNotNull();
  }
}
