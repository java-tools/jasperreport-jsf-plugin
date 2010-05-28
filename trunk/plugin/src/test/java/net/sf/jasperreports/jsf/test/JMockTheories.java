/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.test;

import java.lang.reflect.Field;
import java.util.List;

import org.jmock.Mockery;
import org.junit.experimental.theories.Theories;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 *
 * @author antonio.alonso
 */
public class JMockTheories extends Theories {

    private final Field mockeryField;

    public JMockTheories(Class<?> testClass) throws InitializationError {
        super(testClass);
        mockeryField = findMockeryField(testClass);
        mockeryField.setAccessible(true);
    }

    @Override
    protected List<MethodRule> rules(Object test) {
        List<MethodRule> list = super.rules(test);
        list.add(new AssertionSatisfiedRule());
        return list;
    }

    protected Mockery mockeryOf(Object test) {
        try {
            Mockery mockery = (Mockery)mockeryField.get(test);
            if (mockery == null) {
                throw new IllegalStateException("Mockery named '"
                    + mockeryField.getName() + "' is null");
            }
            return mockery;
        }
        catch (IllegalAccessException e) {
            throw new IllegalStateException("cannot get value of field "
                + mockeryField.getName(), e);
        }
    }

    public class AssertionSatisfiedRule implements MethodRule {

        public Statement apply(final Statement base,
                final FrameworkMethod method, final Object target) {
            return new Statement() {
                public void evaluate() throws Throwable {
                    try {
                        base.evaluate();
                        mockeryOf(target).assertIsSatisfied();
                    } catch (Throwable e) {
                        mockeryOf(target).assertIsSatisfied();
                        throw e;
                    }
                }
            };
        }

    }

    static Field findMockeryField(Class<?> testClass) throws InitializationError {
        for (Class<?> c = testClass; c != Object.class; c = c.getSuperclass()) {
            for (Field field: c.getDeclaredFields()) {
                if (Mockery.class.isAssignableFrom(field.getType())) {
                    return field;
                }
            }
        }

        throw new InitializationError("no Mockery found in test class "
            + testClass);
    }

}
