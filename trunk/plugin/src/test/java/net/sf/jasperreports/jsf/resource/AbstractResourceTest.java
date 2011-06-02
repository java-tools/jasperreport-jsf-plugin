/*
 * JaspertReports JSF Plugin Copyright (C) 2011 A. Alonso Dominguez
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version. This library is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA A.
 *
 * Alonso Dominguez
 * alonsoft@users.sf.net
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
