package com.accenture.pregunton.exception;

import com.accenture.pregunton.util.ModelUtil;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerNotFoundExceptionTest {

  @Test
  public void constructor_ShouldCreateANewInstanceOfGameCodeFoundException() {
    assertThat(new PlayerNotFoundException(ModelUtil.NICK_NAME)).isInstanceOf(PlayerNotFoundException.class)
        .isNotNull();
  }
}
