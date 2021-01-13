package com.accenture.pregunton.pojo;

import org.junit.Test;
import pl.pojo.tester.api.assertion.Method;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class GameDtoTest {

    @Test
    public void testPojo() {
        assertPojoMethodsFor(GameDto.class)
                .testing(Method.GETTER, Method.SETTER, Method.CONSTRUCTOR)
                .areWellImplemented();
    }

}
