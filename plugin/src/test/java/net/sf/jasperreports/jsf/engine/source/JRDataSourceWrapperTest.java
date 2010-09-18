/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.engine.source;

import net.sf.jasperreports.engine.JRDataSource;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author aalonsodominguez
 */
@RunWith(JMock.class)
public class JRDataSourceWrapperTest {

    private Mockery mockery = new JUnit4Mockery();

    @Test
    public void nonNullDSWorksFine() throws Exception {
        final JRDataSource expectedDs = mockery.mock(JRDataSource.class);
        final JRDataSourceWrapper wrapper = new JRDataSourceWrapper(expectedDs);

        JRDataSource ds = wrapper.getDataSource();
        assertThat(ds, notNullValue());
        assertThat(ds, sameInstance(expectedDs));

        wrapper.dispose();
        assertThat(wrapper.getDataSource(), nullValue());
    }

    @Test
    public void nullConnectionThrowsIllegalArgEx() {
        try {
            new JRDataSourceWrapper(null);
            fail("An IllegalArgumentException should be thrown");
        } catch (RuntimeException e) {
            assertThat(e, is(IllegalArgumentException.class));
        }
    }

}
