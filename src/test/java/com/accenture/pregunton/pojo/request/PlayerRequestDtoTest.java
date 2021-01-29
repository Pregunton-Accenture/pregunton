package com.accenture.pregunton.pojo.request;

import org.junit.Test;
import pl.pojo.tester.api.assertion.Method;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class PlayerRequestDtoTest {

  @Test
  public void testPojo() {
    assertPojoMethodsFor(PlayerRequestDto.class)
        .testing(Method.GETTER, Method.SETTER, Method.CONSTRUCTOR, Method.TO_STRING)
        .areWellImplemented();
  }
}
