/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jmock.integration.testng;

import org.jmock.Mockery;
import org.jmock.lib.AssertionErrorTranslator;

/**
 *
 * @author antonio.alonso
 */
public class TestNGMockery extends Mockery {

    public TestNGMockery() {
        setExpectationErrorTranslator(AssertionErrorTranslator.INSTANCE);
    }

}
