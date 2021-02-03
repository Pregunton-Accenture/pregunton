package com.accenture.pregunton.util;

import org.junit.Test;
import pl.pojo.tester.api.assertion.Method;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class SimpleResponseTest {

  @Test
  public void testPojo() {
    assertPojoMethodsFor(SimpleResponse.class).testing(Method.GETTER, Method.SETTER, Method.TO_STRING)
        .areWellImplemented();
  }
}
