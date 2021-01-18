package com.accenture.pregunton.pojo;

import org.junit.Test;
import pl.pojo.tester.api.assertion.Method;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class RuleDtoTest {

    @Test
    public void testPojo() {
        assertPojoMethodsFor(RuleDto.class)
                .testing(Method.GETTER, Method.SETTER, Method.CONSTRUCTOR)
                .areWellImplemented();
    }

}
