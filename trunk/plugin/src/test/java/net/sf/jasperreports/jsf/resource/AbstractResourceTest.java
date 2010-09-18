/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assume.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author aalonsodominguez
 */
@RunWith(Theories.class)
public class AbstractResourceTest {

    @DataPoint
    public static final String NULL_NAME = null;

    @DataPoint
    public static final String EMPTY_NAME = "";

    @DataPoint
    public static final String NON_NULL_NAME = "resource";

    @Theory
    public void nullNameOrEmptyThrowsIllegalArgEx(String name) {
        assumeThat(name, nullValue());

        Resource resource = null;
        try {
            resource = new DummyResource(name);
            fail("A null resource name should throw an IllegalArgumentException");
        } catch (RuntimeException e) {
            assertThat(e, is(IllegalArgumentException.class));
        }
    }

    @Theory
    public void emptyNameThrowsIllegalArgEx(String name) {
        assumeThat(name, notNullValue());
        assumeTrue(name.length() == 0);

        Resource resource = null;
        try {
            resource = new DummyResource(name);
            fail("A null resource name should throw an IllegalArgumentException");
        } catch (RuntimeException e) {
            assertThat(e, is(IllegalArgumentException.class));
        }
    }

    @Theory
    public void nonNullNameWorksFine(String name) {
        assumeThat(name, notNullValue());
        assumeTrue(name.length() > 0);

        Resource resource = new DummyResource(name);
        assertThat(resource, notNullValue());
        assertThat(resource.getName(), equalTo(name));
    }

    private class DummyResource extends AbstractResource {

        public DummyResource(String name) {
            super(name);
        }

        public URL getLocation() throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public InputStream getInputStream() throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public String getPath() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }

}
