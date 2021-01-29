package com.accenture.pregunton.model;

import org.junit.Test;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class CategoryTest {

  @Test
  public void testPojo() {
    assertPojoMethodsFor(Category.class).areWellImplemented();
  }
}
