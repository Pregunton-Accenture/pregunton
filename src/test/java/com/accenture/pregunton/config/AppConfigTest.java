package com.accenture.pregunton.config;

import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class AppConfigTest {

  private AppConfig config;

  @Before
  public void setUp() {
    config = new AppConfig();
  }

  @Test
  public void modelMapper_ShouldReturnModelMapperInstance() {
    assertThat(config.modelMapper()).isInstanceOf(ModelMapper.class)
        .isNotNull();
  }
  @Test
  public void restTemplate_ShouldReturnRestTemplateInstance() {
    assertThat(config.restTemplate()).isInstanceOf(RestTemplate.class)
        .isNotNull();
  }
}
