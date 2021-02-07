package com.accenture.pregunton.exception;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryNotFoundExceptionTest {

  @Test
  public void constructor_ShouldCreateANewInstanceOfCategoryNotFoundException() {
    assertThat(new CategoryNotFoundException(1L)).isInstanceOf(CategoryNotFoundException.class)
        .isNotNull();
  }
}
