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
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.Matchers.*;
import static net.sf.jasperreports.jsf.test.Matchers.*;

/**
 *
 * @author aalonsodominguez
 */
@RunWith(Theories.class)
public class URLResourceTest {

    @DataPoint
    public static URL existantRemoteUrl() {
        try {
            return new URL("http", "jasperreportjsf.sourceforge.net",
                        "/tld/jasperreports-jsf-1_0.tld");
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @DataPoint
    public static URL unexistantRemoteUrl() {
        try {
            return new URL("http", "jasperreportjsf.sourceforge.net",
                        "/tld/jasperreports-jsf-1_0.xml");
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @DataPoint
    public static URL localUrl() {
        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        String baseName = URLResourceTest.class.getName()
                .replaceAll("\\.", "/");
        return classLoader.getResource(baseName);
    }

    @Theory
    public void checkWithExistantURL(URL location) throws Exception {
        assumeThat(location, notNullValue());
        assumeThat(location, existsURL());

        URLResource resource = new URLResource(location);

        assertThat(resource.getLocation(), sameInstance(location));
        assertThat(resource.getName(), equalTo(location.toString()));
        assertThat(resource.getPath(), equalTo(location.getPath()));

        InputStream stream = null;
        try {
            stream = resource.getInputStream();
        } catch (Exception e) {
            assumeNoException(e);
        }

        assertThat(stream, notNullValue());

        try {
            stream.close();
        } catch (IOException e) { }
    }

    @Theory
    @SuppressWarnings("unused")
    public void checkWithUnexistantURL(URL location) throws Exception {
        assumeThat(location, notNullValue());
        assumeThat(location, not(existsURL()));

        URLResource resource = new URLResource(location);

        assertThat(resource.getLocation(), sameInstance(location));
        assertThat(resource.getName(), equalTo(location.toString()));
        assertThat(resource.getPath(), equalTo(location.getPath()));

        InputStream stream = null;
        try {
            stream = resource.getInputStream();
            fail("A I/O exception should be thrown.");
        } catch (Exception e) {
            assertThat(e, is(IOException.class));
        }
    }

}
