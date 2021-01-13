package com.accenture.pregunton.model;

import org.junit.Test;
import pl.pojo.tester.api.assertion.Method;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class QuestionTest {

    @Test
    public void testPojo() {
        assertPojoMethodsFor(Question.class)
                .testing(Method.GETTER, Method.SETTER, Method.CONSTRUCTOR)
                .areWellImplemented();
    }

}
