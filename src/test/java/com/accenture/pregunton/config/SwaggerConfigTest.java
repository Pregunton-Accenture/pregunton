package com.accenture.pregunton.config;

import org.junit.Before;
import org.junit.Test;
import springfox.documentation.spring.web.plugins.Docket;

import static org.assertj.core.api.Assertions.assertThat;

public class SwaggerConfigTest {

  private SwaggerConfig swaggerConfig;

  @Before
  public void setUp() {
    swaggerConfig = new SwaggerConfig();
  }

  @Test
  public void SwaggerConfig_ShouldReturnDocket() {
    assertThat(swaggerConfig.api()).isInstanceOf(Docket.class)
        .isNotNull();
  }
}
