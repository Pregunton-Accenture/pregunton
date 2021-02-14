package com.accenture.pregunton.exception;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryExistsExceptionTest {

  @Test
  public void constructor_ShouldCreateANewInstanceOfCategoryExistsException() {
    assertThat(new CategoryExistsException("category")).isInstanceOf(CategoryExistsException.class)
        .isNotNull();
  }
}
