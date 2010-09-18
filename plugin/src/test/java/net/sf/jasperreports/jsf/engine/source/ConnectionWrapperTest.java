/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.engine.source;

import java.sql.Connection;
import org.jmock.Expectations;
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
public class ConnectionWrapperTest {

    private Mockery mockery = new JUnit4Mockery();

    @Test
    public void nonNullConnectionReceivesDelegations() throws Exception {
        final Connection expectedConn = mockery.mock(Connection.class);

        ConnectionWrapper wrapper = new ConnectionWrapper(expectedConn);

        mockery.checking(new Expectations() {{
            oneOf(expectedConn).close();
        }});

        Connection conn = wrapper.getConnection();
        assertThat(conn, notNullValue());
        assertThat(conn, sameInstance(expectedConn));

        wrapper.dispose();
        assertThat(wrapper.getConnection(), nullValue());
    }

    @Test
    public void nullConnectionThrowsIllegalArgEx() {
        try {
            new ConnectionWrapper(null);
            fail("An IllegalArgumentException should be thrown");
        } catch (RuntimeException e) {
            assertThat(e, is(IllegalArgumentException.class));
        }
    }

}
