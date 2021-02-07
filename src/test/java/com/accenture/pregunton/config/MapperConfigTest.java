package com.accenture.pregunton.config;

import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;

public class MapperConfigTest {

  private MapperConfig mapperConfig;

  @Before
  public void setUp() {
    mapperConfig = new MapperConfig();
  }

  @Test
  public void modelMapper_ShouldReturnModelMapperInstance() {
    assertThat(mapperConfig.modelMapper()).isInstanceOf(ModelMapper.class)
        .isNotNull();
  }
}
